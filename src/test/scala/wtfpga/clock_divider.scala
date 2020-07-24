package wtfpga


package wtfpga

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

import chisel3._
import chisel3.tester._
import chisel3.experimental.BundleLiterals._
import chiseltest.experimental.TestOptionBuilder._
import treadle.WriteVcdAnnotation


// This module does not have an implicit clock and reset since it is a
// RawModule as opposed to a normal Chisel Module.
class ClockDividerDemo extends RawModule {
  val io = IO(new Bundle {
    // Input clock
    val clock = Input(Clock())
    // Reset for the above clock (optional)
    val reset = Input(Bool())
    // Clock divided by 4
    val clock_divBy4 = Output(Clock())
    // Clock divided by 6
    val clock_divBy6 = Output(Clock())
  })

  // Use our ClockDivider function above.
  io.clock_divBy4 := ClockDivider(io.clock, 4, reset = Some(io.reset))
  io.clock_divBy6 := ClockDivider(io.clock, 6, reset = Some(io.reset))
}

// This wrapper module is a regular Chisel module with an implicit
// clock which is driven by the testers.
class ClockDividerDemoWrapper extends Module {
  val io = IO(new Bundle{
    // Clock divided by 4
    val clock_divBy4 = Output(Bool())
    // Clock divided by 6
    val clock_divBy6 = Output(Bool())
  })

  val clock_divider = Module(new ClockDividerDemo)
  clock_divider.io.clock := clock
  clock_divider.io.reset := reset
  // Convert Clocks to Bools for testing.
  io.clock_divBy4 := clock_divider.io.clock_divBy4.asUInt.asBool
  io.clock_divBy6 := clock_divider.io.clock_divBy6.asUInt.asBool
}


class ClockDividerTesters extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "ClockDivider"

    it should "divide correctly" in {
        test(new ClockDividerDemoWrapper()).withAnnotations(Seq(WriteVcdAnnotation)) { mod =>
            // Test for a certain number of cycles.
            val num_cycles = 100
            var cycle = 0

            // Get initial values
            var prev_div4 = mod.io.clock_divBy4.peek().litValue
            var div4_last_change_cycle: Option[Int] = None
            var prev_div6 = mod.io.clock_divBy6.peek().litValue
            var div6_last_change_cycle: Option[Int] = None

            while (cycle < num_cycles) {
                mod.clock.step()
                cycle += 1

                val div4 = mod.io.clock_divBy4.peek().litValue
                val div6 = mod.io.clock_divBy6.peek().litValue

                // If the divided-by-4 clock changed, check that at least 2 cycles have passed.
                if (div4 !== prev_div4) {
                    div4_last_change_cycle match {
                        // divBy4 should change every 2 cycles
                        case Some(last) => assert(cycle - last == 2)
                        case None =>
                    }
                    div4_last_change_cycle = Some(cycle)
                }

                // If the divided-by-6 clock changed, check that at least 3 cycles have passed.
                if (div6 != prev_div6) {
                    div6_last_change_cycle match {
                        // divBy6 should change every 3 cycles
                        case Some(last) =>assert(cycle - last == 3)
                        case None =>
                    }
                    div6_last_change_cycle = Some(cycle)
                }

                prev_div4 = div4
                prev_div6 = div6
            }
        }
    }
}
