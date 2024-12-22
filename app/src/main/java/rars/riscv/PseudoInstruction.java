package rars.riscv;
import java.util.ArrayList;

import rars.simulator.Operand;
import rars.simulator.ProgramStatement;

/**
 * Pseudo instruction interface. Any such instruction must be extended to some basic instructions
 * @see BasicInstruction
 */
public interface PseudoInstruction extends Instruction {
    public ArrayList<ProgramStatement> template(ArrayList<Operand> args);
}
