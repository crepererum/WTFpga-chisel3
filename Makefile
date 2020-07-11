PIN_DEF       := icebreaker.pcf
DEVICE        := up5k
PACKAGE       := sg48
SCALA_MODULE  := wtfpga.WTFpga
MILL_MODULE   := WTFpga
.DEFAULT_GOAL := timing

include main.mk
