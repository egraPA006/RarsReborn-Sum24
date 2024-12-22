package rars.riscv;

import java.util.ArrayList;
import rars.simulator.Operand;

/**
 * Interface for all basic instructions. It is supposed that instructions work directly with registers
 */
public interface BasicInstruction extends Instruction  {
    // FIXME: defenitely wrong approach
    public void simulate(ArrayList<Operand> operands);
}