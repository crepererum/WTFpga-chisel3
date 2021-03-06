// https://github.com/edwardcwang/chisel-multiclock-demo/blob/16bda51558ff6757a5531933621087c8b033ccd9/src/main/scala/multiclock_demo/ClockDividerDemo.scala

package wtfpga

import chisel3._
import chisel3.util.log2Ceil

object ClockDivider {
  /**
    * Create a clock divider.
    * @param clock_in Clock signal to divide.
    * @param divide_by Factor to divide by (e.g. divide by 2). Must be even.
    * @param reset Optional reset signal.
    * @return Divided clock.
    */
  def apply(clock_in: Clock, divide_by: Int, reset: Option[Bool] = None): Clock = {
    require(divide_by % 2 == 0, "Must divide by an even factor")

    // Declare some wires for use in this function.
    val output_clock = Wire(Clock())
    val resetWire = Wire(Bool())
    resetWire := reset.getOrElse(false.B)

    withClockAndReset(clock=clock_in, reset=resetWire) {
      // Divide down by n means that every n/2 cycles, we should toggle
      // the new clock.
      val max: Int = divide_by / 2
      // log2Ceil(n) = the # of bits needed to represent n unique things
      val counter = RegInit(0.U(log2Ceil(max).W))
      counter := counter + 1.U // The counter always increments.

      // Every second cycle, toggle the new divided down clock.
      val dividedDownClock = RegInit(false.B)
      when (counter === (max - 1).U) {
        dividedDownClock := ~dividedDownClock
        counter := 0.U
      }

      // Connect the register for the divided down clock to the output IO.
      output_clock := dividedDownClock.asClock
    }
    output_clock
  }
}
