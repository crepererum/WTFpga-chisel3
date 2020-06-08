pin_def     := "icebreaker.pcf"
device      := "up5k"
package     := "sg48"
top_package := "wtfpga"
top_module  := "WTFpga"

default: timing

setup:
    mkdir -p build

compile: setup
    rm -f build/*.v
    sbt "runMain chisel3.stage.ChiselMain --module {{top_package}}.{{top_module}} --target-dir build"

synthesis: compile
    rm -f build/*.synthesis.json build/synthesis.log
    yosys \
        -q \
        -l build/synthesis.log \
        -p 'synth_ice40 -top {{top_module}} -json build/main.synthesis.json' \
        build/*.v

place_and_route: synthesis
    rm -f build/main.asc
    nextpnr-ice40 \
        --{{device}} \
        --package {{package}} \
        --json build/main.synthesis.json \
        --pcf {{pin_def}} \
        --asc build/main.asc

timing: place_and_route
    rm -f build/main.rpt
    icetime \
        -d {{device}} \
        -m \
        -t \
        -r build/main.rpt \
        build/main.asc

bitstream: place_and_route
    icepack build/main.asc build/main.bin

program: bitstream
    iceprog build/main.bin
