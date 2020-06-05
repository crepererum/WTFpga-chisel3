pin_def := "icebreaker.pcf"
device  := "up5k"

default: timing

setup:
    mkdir -p build

synthesis: setup
    yosys \
        -q \
        -l build/synthesis.log \
        -p 'synth_ice40 -top top -json build/main.json' \
        src/*.v

place_and_route: synthesis
    nextpnr-ice40 \
        --{{device}} \
        --json build/main.json \
        --pcf {{pin_def}} \
        --asc build/main.asc

timing: place_and_route
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
