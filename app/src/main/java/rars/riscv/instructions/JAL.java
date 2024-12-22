package rars.riscv.instructions;

import java.util.ArrayList;
import java.util.List;

import rars.Globals;
import rars.assembler.Token;
import rars.assembler.TokenType;
import rars.riscv.BasicInstruction;
import rars.simulator.ImOperand;
import rars.simulator.Operand;
import rars.simulator.RegOperand;
import rars.riscv.registers.PC;

public class JAL implements BasicInstruction {
    public void simulate(ArrayList<Operand> args){
        ((RegOperand)args.get(0)).getValue().setValue(Globals.registerTable.get("pc").getValue()+4);
        ((PC)Globals.registerTable.get("pc")).increment(((ImOperand)args.get(1)).getValue());
    }
    public boolean checkOperands(List<Token> args) {
        if (args.size() != 2) return false;
        if (args.get(0).getType().equals(TokenType.REGISTER) && args.get(1).getType().equals(TokenType.LABEL)) return true;
        return false;
    }
}
