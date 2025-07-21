package cs

import circt.stage.ChiselStage

object BlinkyMain extends App {
  ChiselStage.emitSystemVerilogFile(
    new Blinky,
    Array("--target-dir", "build"),  // Chisel args: Set output directory
    Array("-disable-mem-randomization", "-disable-reg-randomization")  // firtool opts: Disable randomization for synthesis
  )
}
