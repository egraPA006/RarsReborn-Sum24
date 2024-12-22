package rars.riscv.instructions;

import rars.riscv.BasicInstruction;
import java.util.ArrayList;
import rars.riscv.registers.PC;
import rars.simulator.ImOperand;
import rars.Globals;
import rars.assembler.Token;
import java.util.List;
import rars.simulator.Operand;
import rars.simulator.RegOperand;
import rars.assembler.TokenType;

public class BLT implements BasicInstruction {
    public void simulate(ArrayList<Operand> args){
        if (((RegOperand) args.get(0)).getValue().getValue() < ((RegOperand) args.get(1)).getValue().getValue()) {
            ((PC)Globals.registerTable.get("pc")).increment(((ImOperand)args.get(2)).getValue());
        }
    }
    public boolean checkOperands(List<Token> args) {
        if (args.size() != 3) return false;
        if (args.get(0).getType().equals(TokenType.REGISTER) && 
            args.get(1).getType().equals(TokenType.REGISTER) && 
            args.get(2).getType().equals(TokenType.LABEL)) return true;
        return false;
    }
}
    
