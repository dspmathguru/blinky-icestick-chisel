PROJ = Blinky
BUILD_DIR = build
MILL = mill
YOSYS = yosys
NEXTPNR = nextpnr-ice40
ICEPACK = icepack
ICEPROG = iceprog
PCF = constraints/blinky.pcf
DEVICE = hx1k
PACKAGE = tq144

all: $(BUILD_DIR)/$(PROJ).bin

$(BUILD_DIR)/$(PROJ).sv:
	$(MILL) cs.runMain cs.BlinkyMain

$(BUILD_DIR)/$(PROJ).json: $(BUILD_DIR)/$(PROJ).sv
	$(YOSYS) -p "synth_ice40 -top Blinky -json $@" $<  # Updated to -top Blinky

$(BUILD_DIR)/$(PROJ).asc: $(BUILD_DIR)/$(PROJ).json
	$(NEXTPNR) --$(DEVICE) --package $(PACKAGE) --pcf $(PCF) --json $< --asc $@

$(BUILD_DIR)/$(PROJ).bin: $(BUILD_DIR)/$(PROJ).asc
	$(ICEPACK) $< $@

prog: $(BUILD_DIR)/$(PROJ).bin
	$(ICEPROG) $<

test:
	$(MILL) cs.test

view: build/chiselsim/blinky.vcd
	gtkwave $<

build/chiselsim/blinky.vcd:
	$(MILL) cs.test -- -DemitVcd=1

clean-mill:
	$(MILL) clean

clean: clean-mill
	rm -rf $(BUILD_DIR)/*

.PHONY: all prog test view clean clean-mill
