package wtfpga

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}

class WTFpga extends MultiIOModule {
  val i_btn1  = IO(Input(Bool()))     suggestName("BTN1")
  // val i_btn2  = IO(Input(Bool()))     suggestName("BTN2")
  // val i_btn3  = IO(Input(Bool()))     suggestName("BTN3")
  // val i_btn_n = IO(Input(Bool()))     suggestName("BTN_N")
  // val i_sw    = IO(Input(UInt(8.W)))  suggestName("sw")
  val o_led   = IO(Output(UInt(5.W))) suggestName("led")
  // val o_seg   = IO(Output(UInt(7.W))) suggestName("seg")
  // val o_ca    = IO(Output(Bool()))    suggestName("ca")

  o_led := Cat(i_btn1, 0.U)
}


object MainDriver extends App {
  (new ChiselStage).execute(args, Seq(ChiselGeneratorAnnotation(() => new WTFpga)))
}
