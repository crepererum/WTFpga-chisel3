SCALA_BUILD_FILES  = build.sc
SCALA_SOURCE_FILES = $(shell find src/main -type f -name '*.scala')
SCALA_TEST_FILES   = $(shell find src/test -type f -name '*.scala')
TOP_MODULE         = $(lastword $(subst ., ,$(SCALA_MODULE)))
VERILOG_TOP_MODULE = build/$(TOP_MODULE).v
YOSYS_OUTPUT       = build/main.synthesis.json
NEXTPNR_OUTPUT     = build/main.asc
TIMING_REPORT      = build/main.rpt
BITSTREAM_FILE     = build/main.bin

$(VERILOG_TOP_MODULE): $(SCALA_BUILD_FILES) $(SCALA_SOURCE_FILES)
	mkdir -p build && \
	scripts/mill "$(MILL_MODULE).runMain" \
		--mainClass chisel3.stage.ChiselMain \
		--module "$(SCALA_MODULE)" \
		--target-dir build

$(YOSYS_OUTPUT): $(VERILOG_TOP_MODULE)
	yosys \
		-q \
		-l build/synthesis.log \
		-p 'synth_ice40 -top $(TOP_MODULE) -json $@' \
		$<

$(NEXTPNR_OUTPUT): $(YOSYS_OUTPUT) $(PIN_DEF)
	nextpnr-ice40 \
		--$(DEVICE) \
		--package $(PACKAGE) \
		--json $< \
		--pcf $(PIN_DEF) \
		--asc $@

$(TIMING_REPORT): $(NEXTPNR_OUTPUT)
	icetime \
		-d $(DEVICE) \
		-m \
		-t \
		-r $@ \
		$<

timing: $(TIMING_REPORT)

$(BITSTREAM_FILE): $(NEXTPNR_OUTPUT)
	icepack $< $@

flash: $(BITSTREAM_FILE)
	iceprog $<

test: $(SCALA_SOURCE_FILES) $(SCALA_TEST_FILES)
	scripts/mill "$(MILL_MODULE).test"

clean:
	rm -rf build

.PHONY: clean flash test timing
