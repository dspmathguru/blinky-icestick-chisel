package cs

import chisel3._
import chisel3.util._

class Blinky extends RawModule {
  val clk = IO(Input(Clock()))
  val led = IO(Output(Bool()))

  val counter = withClockAndReset(clk, false.B) { RegInit(0.U(25.W)) }  // Explicit clock and no-reset for init

  withClockAndReset(clk, false.B) {
    counter := counter + 1.U
  }
  led := counter(24)  // Toggle LED using the MSB (blinks at ~0.375 Hz)
}
