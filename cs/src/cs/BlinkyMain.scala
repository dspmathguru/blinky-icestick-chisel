package cs

import circt.stage.ChiselStage
import chisel3._

object BlinkyMain extends App {
  class BlinkyWrapper extends RawModule {  // Wrapper to tie rst low
    val clk = IO(Input(Clock()))
    val led = IO(Output(Vec(4, Bool())))

    val bl = Module(new Blinky)
    bl.clk := clk
    bl.rst := false.B  // Tie rst to false (no external pin)
    led := bl.led
  }
  ChiselStage.emitSystemVerilogFile(
    new BlinkyWrapper,
    Array("--target-dir", "build"),
    Array("-disable-mem-randomization", "-disable-reg-randomization")
  )
}
