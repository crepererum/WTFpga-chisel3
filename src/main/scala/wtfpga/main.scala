package wtfpga

import chisel3._
import chisel3.util._
import chisel3.util.experimental.forceName

class WTFpga extends RawModule {
  val clock   = IO(Input(Clock()))
  val i_btn1  = IO(Input(Bool()))
  val i_btn2  = IO(Input(Bool()))
  val i_btn3  = IO(Input(Bool()))
  val i_btn_n = IO(Input(Bool()))
  val i_sw    = IO(Input(UInt(8.W)))
  val o_led   = IO(Output(UInt(5.W)))
  val o_seg   = IO(Output(UInt(7.W)))
  val o_ca    = IO(Output(Bool()))

  // force names of external signals to match PCF
  forceName(clock,   "CLK")
  forceName(i_btn1,  "BTN1")
  forceName(i_btn2,  "BTN2")
  forceName(i_btn3,  "BTN3")
  forceName(i_btn_n, "BTN_N")
  forceName(i_sw,    "sw")
  forceName(o_led,   "led")
  forceName(o_seg,   "seg")
  forceName(o_ca,    "ca")

  // Mock reset signal
  val nothing = Wire(Bool())
  nothing := DontCare

  withClockAndReset(clock, nothing) {
    val dispValue   = Wire(UInt(8.W))
    val sum         = Wire(UInt(8.W))
    val div         = Wire(UInt(8.W))
    val disp0       = Wire(UInt(8.W))
    val disp1       = Wire(UInt(8.W))
    val storedValue = Reg(UInt(8.W))

    when(i_btn_n) {
      storedValue := i_sw
    }

    sum := i_sw + storedValue
    div := i_sw - storedValue

    when(i_btn3) {
      dispValue := div
    }.otherwise {
      when(i_btn1) {
        dispValue := sum
      }.otherwise {
        when(i_btn2) {
          dispValue := storedValue
        }.otherwise {
          dispValue := i_sw
        }
      }
    }

    val nibble0 = Module(new NibbleToSevenSeg)
    nibble0.io.nibblein := dispValue(3, 0)
    disp0 := nibble0.io.segout

    val nibble1 = Module(new NibbleToSevenSeg)
    nibble1.io.nibblein := dispValue(7, 4)
    disp1 := nibble1.io.segout

    val display = Module(new SevenSegMux)
    display.clock := ClockDivider(clock, 16)
    display.io.disp0 := disp0
    display.io.disp1 := disp1
    o_seg := display.io.segout
    o_ca := display.io.disp_sel

    o_led := Cat(i_btn1, 0.U)
  }
}
