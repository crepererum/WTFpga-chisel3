# WTFpga with Chisel3

This is a port of [WTFpga](https://github.com/esden/WTFpga) to [chisel3](https://www.chisel-lang.org/).

[![Build Status](https://github.com/crepererum/WTFpga-chisel3/workflows/CI/badge.svg)](https://github.com/crepererum/WTFpga-chisel3/actions?query=workflow%3ACI)


## Requirements

- [Scala](https://scala-lang.org/)
- [Open Source FPGA Toolchain](https://github.com/open-tool-forge/fpga-toolchain)


## Usage
To get the timing report just run:

```sh
make
```

To run the Chisel3/Scala unit tests, use:

```sh
make test
```

To flash the device, run:

```sh
make flash
```


## IDE

1. Get [VSCode](https://code.visualstudio.com/)
2. Install the following extensions:
   1. ["Scala (Metals)"](https://marketplace.visualstudio.com/items?itemName=scalameta.metals)
   2. ["EditorConfig for VS Code"](https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig)


## References

- https://github.com/edwardcwang/chisel-multiclock-demo/
- https://github.com/freechipsproject/chisel-template/
- https://github.com/olafurpg/setup-scala
- https://github.com/open-tool-forge/fpga-toolchain
- https://stackoverflow.com/questions/40470153/is-there-a-simple-example-of-how-to-generate-verilog-from-chisel3-module
- https://stackoverflow.com/questions/41427717/how-to-delete-clock-signal-on-chisel3-top-module
- https://stackoverflow.com/questions/55209951/chisel3-how-to-create-a-register-without-reset-signal-in-rawmodule
- https://stackoverflow.com/questions/59407466/how-to-override-extend-chisel-signal-naming/59411596#59411596
