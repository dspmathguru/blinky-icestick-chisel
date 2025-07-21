PROJ = Blinky
BUILD_DIR = build
MILL = mill
YOSYS = yosys
NEXTPNR = nextpnr-ice40
ICEPACK = icepack
ICEPROG = iceprog
PCF = blinky.pcf
DEVICE = hx1k
PACKAGE = tq144:4k

all: $(BUILD_DIR)/$(PROJ).bin

$(BUILD_DIR)/$(PROJ).sv:
	$(MILL) cs.runMain cs.BlinkyMain

$(BUILD_DIR)/$(PROJ).json: $(BUILD_DIR)/$(PROJ).sv
	$(YOSYS) -p "synth_ice40 -top Blinky -json $@" $<

$(BUILD_DIR)/$(PROJ).asc: $(BUILD_DIR)/$(PROJ).json
	$(NEXTPNR) --$(DEVICE) --package $(PACKAGE) --pcf $(PCF) --json $< --asc $@

$(BUILD_DIR)/$(PROJ).bin: $(BUILD_DIR)/$(PROJ).asc
	$(ICEPACK) $< $@

prog: $(BUILD_DIR)/$(PROJ).bin
	$(ICEPROG) $<

view: build/chiselsim/blinky.vcd
	gtkwave $<

build/chiselsim/blinky.vcd:
	$(MILL) cs.test

clean:
	rm -f $(BUILD_DIR)/Blinky.* $(BUILD_DIR)/layers-*  # Clean main files and extra verification files

.PHONY: all prog view clean
