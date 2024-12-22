package rars.riscv.instructions;

import rars.riscv.BasicInstruction;
import rars.simulator.Operand;

import java.util.List;
import java.util.ArrayList;
import rars.assembler.Token;

public class SB implements BasicInstruction {
    public boolean checkOperands(List<Token> args) {
        return true;
    }
    public void simulate(ArrayList<Operand> args) {

    }
}
