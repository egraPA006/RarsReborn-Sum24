package rars.riscv.instructions;
import rars.riscv.*;

import java.util.ArrayList;

import rars.simulator.ImOperand;
import rars.simulator.Operand;
import rars.simulator.ProgramStatement;
import rars.simulator.RegOperand;

import java.util.List;

import rars.Globals;
import rars.assembler.Token;
import rars.assembler.TokenType;
public class MV implements PseudoInstruction {
    public boolean checkOperands(List<Token> args) {
        if (args.size() != 2) return false;
        if ((args.get(0).getType().equals(TokenType.REGISTER)) &&
            (args.get(1).getType().equals(TokenType.REGISTER))) return true;
        return false;
    }
    public ArrayList<ProgramStatement> template(ArrayList<Operand> args) {
        ArrayList<ProgramStatement> ans = new ArrayList<>();
        ans.add(new ProgramStatement());
        ans.get(0).instruction = Globals.instructionMap.get("add");
        ans.get(0).basicLine = "add " + ((RegOperand) args.get(0)).getValue().getName() +
             " " + ((RegOperand) args.get(1)).getValue().getName() + " zero";
        ArrayList<Operand> operands = new ArrayList<>();
        operands.add(args.get(0));
        operands.add(args.get(1));
        operands.add(new RegOperand(Globals.registerTable.get("zero")));
        ans.get(0).operands = operands;
        return ans;
    }
}