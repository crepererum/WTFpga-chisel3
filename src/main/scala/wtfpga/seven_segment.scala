package wtfpga

import chisel3._
import chisel3.util._

class SevenSegMux extends Module {
  val io = IO(new Bundle {
    val disp0    = Input(UInt(7.W))
    val disp1    = Input(UInt(7.W))
    val segout   = Output(UInt(7.W))
    val disp_sel = Output(Bool())
  })

  val current = RegInit(false.B)

  when(current) {
    io.segout := io.disp1
    current := false.B
    io.disp_sel := false.B
  }.otherwise {
    io.segout := io.disp0
    current := true.B
    io.disp_sel := true.B
  }
}


class NibbleToSevenSeg extends Module {
  val io = IO(new Bundle {
    val nibblein = Input(UInt(4.W))
    val segout   = Output(UInt(7.W))
  })

  // default case
  io.segout := ~"b1001001".asUInt(7.W)

  switch(io.nibblein) {
    is(0x0.U) {
      io.segout := ~"b0111111".asUInt(7.W)
    }
    is(0x1.U) {
      io.segout := ~"b0000110".asUInt(7.W)
    }
    is(0x2.U) {
      io.segout := ~"b1011011".asUInt(7.W)
    }
    is(0x3.U) {
      io.segout := ~"b1001111".asUInt(7.W)
    }
    is(0x4.U) {
      io.segout := ~"b1100110".asUInt(7.W)
    }
    is(0x5.U) {
      io.segout := ~"b1101101".asUInt(7.W)
    }
    is(0x6.U) {
      io.segout := ~"b1111101".asUInt(7.W)
    }
    is(0x7.U) {
      io.segout := ~"b0000111".asUInt(7.W)
    }
    is(0x8.U) {
      io.segout := ~"b1111111".asUInt(7.W)
    }
    is(0x9.U) {
      io.segout := ~"b1101111".asUInt(7.W)
    }
    is(0xA.U) {
      io.segout := ~"b1110111".asUInt(7.W)
    }
    is(0xB.U) {
      io.segout := ~"b1111100".asUInt(7.W)
    }
    is(0xC.U) {
      io.segout := ~"b0111001".asUInt(7.W)
    }
    is(0xD.U) {
      io.segout := ~"b1011110".asUInt(7.W)
    }
    is(0xE.U) {
      io.segout := ~"b1111001".asUInt(7.W)
    }
    is(0xF.U) {
      io.segout := ~"b1110001".asUInt(7.W)
    }
  }
}
