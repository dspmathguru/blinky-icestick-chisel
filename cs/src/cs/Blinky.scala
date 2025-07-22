// Blinky.scala
// By: rjjt 
// Date: 2025-07-21
// Description: A simple blinking LED controller using a counter
// License: MIT

package cs

import chisel3._
import chisel3.util._

class Blinky(counterWidth: Int = 22) extends RawModule {
  val clk = IO(Input(Clock()))
  val rst = IO(Input(Bool()))  // New: Reset input (high-active for init)
  val led = IO(Output(Vec(4, Bool())))

  val counter = withClockAndReset(clk, rst) { RegInit(0.U(counterWidth.W)) }  // Use rst for init

  withClockAndReset(clk, rst) {
    counter := counter + 1.U
  }
  val select = counter(counterWidth - 1, counterWidth - 2)
  led(0) := ~(select === 0.U)
  led(1) := ~(select === 1.U)
  led(2) := ~(select === 2.U)
  led(3) := ~(select === 3.U)
}