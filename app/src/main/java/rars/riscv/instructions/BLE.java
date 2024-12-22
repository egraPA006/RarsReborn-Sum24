package rars.riscv.instructions;

import java.util.ArrayList;
import java.util.List;

import rars.Globals;
import rars.assembler.Token;
import rars.assembler.TokenType;
import rars.riscv.BasicInstruction;
import rars.riscv.PseudoInstruction;
import rars.simulator.ImOperand;
import rars.simulator.LabelOperand;
import rars.simulator.Operand;
import rars.simulator.ProgramStatement;
import rars.simulator.RegOperand;
import rars.riscv.registers.PC;

public class BLE implements PseudoInstruction {
    public ArrayList<ProgramStatement> template(ArrayList<Operand> args){
        ArrayList<ProgramStatement> ans = new ArrayList<>();
        ans.add(new ProgramStatement());
        ans.get(0).instruction = Globals.instructionMap.get("bge");
        ans.get(0).basicLine = "bge " + ((RegOperand)args.get(1)).getValue().getName() + " " +
            ((RegOperand)args.get(0)).getValue().getName() + " " + ((LabelOperand) args.get(2)).getValue();
        ArrayList<Operand> operands = new ArrayList<>();
        operands.add(args.get(1));
        operands.add(args.get(0));
        operands.add(args.get(2));
        ans.get(0).operands = operands;
        return ans;
    }
    public boolean checkOperands(List<Token> args) {
        if (args.size() != 3) return false;
        if (args.get(0).getType().equals(TokenType.REGISTER) && 
            args.get(1).getType().equals(TokenType.REGISTER) && 
            args.get(2).getType().equals(TokenType.LABEL)) return true;
        return false;
    }
}
