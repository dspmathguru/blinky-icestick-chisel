package cs

import chisel3._
import chisel3.simulator._
import org.scalatest.funspec.AnyFunSpec

class BlinkyTestHarness(counterWidth: Int = 4) extends Module {
  val bl = Module(new Blinky(counterWidth))  // Pass counterWidth
  bl.clk := clock
  bl.rst := reset  // Connect to implicit reset
  val led = IO(Output(Vec(4, Bool())))
  led := bl.led
}

class BlinkyTest extends AnyFunSpec with chisel3.simulator.scalatest.ChiselSim {
  describe("Blinky") {
    it("should cycle through 4 LEDs") {
      val counterWidth = 4  // For fast simulation
      simulate(new BlinkyTestHarness(counterWidth)) { dut =>
        // Pulse reset to apply init (counter = 0)
        dut.clock.step(1)  // Advance to apply reset if needed
        // Initial state: LED0 on (active-low), others off
        dut.led(0).expect(false.B)
        dut.led(1).expect(true.B)
        dut.led(2).expect(true.B)
        dut.led(3).expect(true.B)
        // Step to next state (select = 01, LED1 on)
        dut.clock.step(1 << (counterWidth - 2))  // 4 cycles
        dut.led(0).expect(true.B)
        dut.led(1).expect(false.B)
        dut.led(2).expect(true.B)
        dut.led(3).expect(true.B)
        // Step to LED2
        dut.clock.step(1 << (counterWidth - 2))
        dut.led(0).expect(true.B)
        dut.led(1).expect(true.B)
        dut.led(2).expect(false.B)
        dut.led(3).expect(true.B)
        // Step to LED3
        dut.clock.step(1 << (counterWidth - 2))
        dut.led(0).expect(true.B)
        dut.led(1).expect(true.B)
        dut.led(2).expect(true.B)
        dut.led(3).expect(false.B)
      }
    }
  }
}
