package cs

import chisel3._
import chisel3.simulator._
import org.scalatest.funspec.AnyFunSpec

// Test harness wrapper Module to provide implicit clock
class BlinkyTestHarness(counterWidth: Int = 4) extends Module {  // Smaller width for fast testing
  val bl = Module(new Blinky(counterWidth))
  bl.clk := clock
  val led = IO(Output(Bool()))
  led := bl.led
}

// Parameterized Blinky for testing (original remains unchanged)
class Blinky(counterWidth: Int) extends RawModule {
  val clk = IO(Input(Clock()))
  val led = IO(Output(Bool()))

  val counter = withClock(clk) { RegInit(0.U(counterWidth.W)) }

  counter := counter + 1.U
  led := counter(counterWidth - 1)  // Toggle using MSB
}

class BlinkyTest extends AnyFunSpec with chisel3.simulator.scalatest.ChiselSim {
  describe("Blinky") {
    it("should toggle LED after clock cycles") {
      simulate(new BlinkyTestHarness()) { dut =>
        // Initial state
        dut.led.expect(false.B)

        // Step half the period (2^(width-1) cycles) and check toggle
        dut.clock.step(1 << (3))  // For width=4, step 8 cycles to toggle
        dut.led.expect(true.B)

        // Step another half and check back to initial
        dut.clock.step(1 << (3))
        dut.led.expect(false.B)
      }
    }
  }
}
