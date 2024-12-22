package rars.simulator;

import rars.riscv.BasicInstruction;
import rars.Globals;
import rars.Logger;
import rars.riscv.*;
import rars.riscv.registers.PC;
import rars.api.TellFrontend;
public class Simulator {
    private RiscvProgram program;
    private PC pc;
    public Simulator() {
        this.program = Globals.riscvProgram;
        pc = (PC)Globals.registerTable.get("pc");
        pc.setValue(0);
    }
    public boolean simulateLine() {
        if (pc.getValue() >= program.basicCode.size()) return false;
        ProgramStatement statement = program.basicCode.get(pc.getValue());
        ((BasicInstruction)statement.instruction).simulate(statement.operands);
        TellFrontend.highlightCurrentLine();
        pc.increment(1);
        Globals.logger.debug("Line executed");
        return true;
    }
}
