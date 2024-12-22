package rars.simulator;

import rars.riscv.*;
import java.util.ArrayList;
/**
 * ProgramStatement
 */
public class ProgramStatement {
    public SourceLine sourceLine;
    public String basicLine;
    // FIXME: really string???
    public String machineLine;
    public Instruction instruction;
    public ArrayList<Operand> operands;
    public int memoryAddress;
    public String label;
}