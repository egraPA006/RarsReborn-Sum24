package rars.riscv.instructions;

import rars.assembler.Token;
import rars.assembler.TokenType;
import rars.riscv.BasicInstruction;
import rars.simulator.Operand;
import rars.simulator.RegOperand;
import java.util.ArrayList;
import java.util.List;
/**
 * LB
 */
public class LB implements BasicInstruction {
    public void simulate(ArrayList<Operand> args) {
        // TODO: WRITE something
    }
    public boolean checkOperands(List<Token> args) {
        return true; // FIXME: AZIZ PLEASE IMPLEMENT NEW TOKENIZER FEAUTURES ^|_|^
    }
}

