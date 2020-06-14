# WTFpga with Chisel3

This is a port of [WTFpga](https://github.com/esden/WTFpga) to [chisel3](https://www.chisel-lang.org/).

## Usage
To get the timing report just run:

```sh
make
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

- https://github.com/freechipsproject/chisel-template/
- https://stackoverflow.com/questions/40470153/is-there-a-simple-example-of-how-to-generate-verilog-from-chisel3-module
- https://stackoverflow.com/questions/59407466/how-to-override-extend-chisel-signal-naming/59411596#59411596
- https://stackoverflow.com/questions/41427717/how-to-delete-clock-signal-on-chisel3-top-module
- https://stackoverflow.com/questions/55209951/chisel3-how-to-create-a-register-without-reset-signal-in-rawmodule
