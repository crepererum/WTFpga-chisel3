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

  val current = Reg(Bool())

  io.disp_sel := current

  when(current) {
    io.segout := io.disp0
    current := false.B
  }.otherwise {
    io.segout := io.disp1
    current := true.B
  }
}


class NibbleToSevenSeg extends Module {
  val io = IO(new Bundle {
    val nibblein = Input(UInt(4.W))
    val segout   = Output(UInt(7.W))
  })

  // default case
  io.segout := ~Integer.parseInt("1001001", 2).U

  switch(io.nibblein) {
    is(0x0.U) {
      io.segout := ~Integer.parseInt("0111111", 2).U
    }
    is(0x1.U) {
      io.segout := ~Integer.parseInt("0000110", 2).U
    }
    is(0x2.U) {
      io.segout := ~Integer.parseInt("1011011", 2).U
    }
    is(0x3.U) {
      io.segout := ~Integer.parseInt("1001111", 2).U
    }
    is(0x4.U) {
      io.segout := ~Integer.parseInt("1100110", 2).U
    }
    is(0x5.U) {
      io.segout := ~Integer.parseInt("1101101", 2).U
    }
    is(0x6.U) {
      io.segout := ~Integer.parseInt("1111101", 2).U
    }
    is(0x7.U) {
      io.segout := ~Integer.parseInt("0000111", 2).U
    }
    is(0x8.U) {
      io.segout := ~Integer.parseInt("1111111", 2).U
    }
    is(0x9.U) {
      io.segout := ~Integer.parseInt("1101111", 2).U
    }
    is(0xA.U) {
      io.segout := ~Integer.parseInt("1110111", 2).U
    }
    is(0xB.U) {
      io.segout := ~Integer.parseInt("1111100", 2).U
    }
    is(0xC.U) {
      io.segout := ~Integer.parseInt("0111001", 2).U
    }
    is(0xD.U) {
      io.segout := ~Integer.parseInt("1011110", 2).U
    }
    is(0xE.U) {
      io.segout := ~Integer.parseInt("1111001", 2).U
    }
    is(0xF.U) {
      io.segout := ~Integer.parseInt("1110001", 2).U
    }
  }
}
