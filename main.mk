SCALA_SOURCE_FILES = $(shell find src/main -type f -name '*.scala')
VERILOG_TOP_MODULE = build/$(TOP_MODULE).v
YOSYS_OUTPUT       = build/main.synthesis.json
NEXTPNR_OUTPUT     = build/main.asc
TIMING_REPORT      = build/main.rpt
BITSTREAM_FILE     = build/main.bin

$(VERILOG_TOP_MODULE): $(SCALA_SOURCE_FILES)
	mkdir -p build && \
	sbt "runMain chisel3.stage.ChiselMain --module $(TOP_PACKAGE).$(TOP_MODULE) --target-dir build"

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

clean:
	rm -rf build

.PHONY: clean flash timing
