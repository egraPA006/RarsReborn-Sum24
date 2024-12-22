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

public class J implements PseudoInstruction {
    public ArrayList<ProgramStatement> template(ArrayList<Operand> args){
        ArrayList<ProgramStatement> ans = new ArrayList<>();
        ans.add(new ProgramStatement());
        ans.get(0).instruction = Globals.instructionMap.get("jal");
        ans.get(0).basicLine = "jal zero, " + ((LabelOperand) args.get(0)).getValue();
        ArrayList<Operand> operands = new ArrayList<>();
        operands.add(new RegOperand(Globals.registerTable.get("zero")));
        operands.add(args.get(0));
        ans.get(0).operands = operands;
        return ans;
    }
    public boolean checkOperands(List<Token> args) {
        if (args.size() != 1) return false;
        if (args.get(0).getType().equals(TokenType.LABEL)) return true;
        return false;
    }
}
