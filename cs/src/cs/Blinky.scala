package cs

import chisel3._
import chisel3.util._

class Blinky extends RawModule {
  val clk = IO(Input(Clock()))
  val rst = IO(Input(Bool()))  // New: Reset input (high-active for init)
  val led = IO(Output(Vec(4, Bool())))

  val counter = withClockAndReset(clk, rst) { RegInit(0.U(22.W)) }  // Use rst for init

  withClockAndReset(clk, rst) {
    counter := counter + 1.U
  }
  val select = counter(counter.getWidth - 1, counter.getWidth - 2)
  led(0) := ~(select === 0.U)
  led(1) := ~(select === 1.U)
  led(2) := ~(select === 2.U)
  led(3) := ~(select === 3.U)
}
