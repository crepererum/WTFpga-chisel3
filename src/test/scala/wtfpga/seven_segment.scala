package wtfpga

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

import chisel3._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import treadle.WriteVcdAnnotation


class SevenSegMuxTesters extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "SevenSegMux"

    it should "support 7 bits" in {
        test(new SevenSegMux()) { mod =>
            mod.io.disp0.poke(127.U)
            mod.io.disp1.poke(127.U)
            mod.io.segout.expect(127.U)

            mod.io.disp0.poke(126.U)
            mod.io.disp1.poke(126.U)
            mod.io.segout.expect(126.U)
        }
    }

    it should "switch outputs" in {
        test(new SevenSegMux()).withAnnotations(Seq(WriteVcdAnnotation)) { mod =>
            mod.io.disp0.poke(1.U)
            mod.io.disp1.poke(2.U)

            mod.io.disp_sel.expect(true.B)
            mod.io.segout.expect(1.U)

            mod.clock.step()
            mod.io.disp_sel.expect(false.B)
            mod.io.segout.expect(2.U)

            mod.clock.step()
            mod.io.disp_sel.expect(true.B)
            mod.io.segout.expect(1.U)
        }
    }

    it should "not buffer" in {
        test(new SevenSegMux()) { mod =>
            mod.io.disp0.poke(1.U)
            mod.io.disp1.poke(1.U)
            mod.io.segout.expect(1.U)

            mod.clock.step()
            mod.io.disp0.poke(2.U)
            mod.io.disp1.poke(2.U)
            mod.io.segout.expect(2.U)

            mod.clock.step()
            mod.io.disp0.poke(3.U)
            mod.io.disp1.poke(3.U)
            mod.io.segout.expect(3.U)
        }
    }
}


class NibbleToSevenSegTesters extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "NibbleToSevenSeg"

    it should "convert 1 correctly" in {
        test(new NibbleToSevenSeg()) { mod =>
            mod.io.nibblein.poke(0.U)
            mod.io.segout.expect(64.U)
        }
    }
}
