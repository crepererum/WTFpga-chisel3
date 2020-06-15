package wtfpga

import org.scalatest._

import chisel3._
import chisel3.tester._
import chisel3.experimental.BundleLiterals._


class NibbleToSevenSegTesters extends FlatSpec with ChiselScalatestTester {
    behavior of "NibbleToSevenSeg"

    it should "convert 1 correctly" in {
        test(new NibbleToSevenSeg()) { mod =>
            mod.io.nibblein.poke(0.U)
            mod.io.segout.expect(64.U)
        }
    }
}
