package rars.riscv.instructions;

import rars.assembler.Token;
import rars.assembler.TokenType;
import rars.riscv.BasicInstruction;
import rars.simulator.ImOperand;
import rars.simulator.Operand;
import rars.simulator.RegOperand;

import java.util.ArrayList;
import java.util.List;

public class NOT implements BasicInstruction {
    public void simulate(ArrayList<Operand> args) {
        int ans = ~(((RegOperand) args.get(1)).getValue().getValue());
        ((RegOperand) args.get(0)).getValue().setValue(ans);
    }
    public boolean checkOperands(List<Token> args) {
        if (args.size() != 2) return false;
        if (args.get(0).getType().equals(TokenType.REGISTER) &&
                (args.get(1).getType().equals(TokenType.REGISTER) )) return true;
        return false;
    }
}
