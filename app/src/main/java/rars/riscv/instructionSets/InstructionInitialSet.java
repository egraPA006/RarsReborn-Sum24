package rars.riscv.instructionSets;

import java.util.HashMap;
import rars.riscv.instructions.*;
import rars.riscv.Instruction;
import rars.riscv.InstructionTable;

/*
 * Simple instruction set for mvp. Will be extended to RV32I
 */
public class InstructionInitialSet implements InstructionTable {
    private HashMap<String, Instruction> instructionSet;
    public InstructionInitialSet() {
        instructionSet = new HashMap<>();
        instructionSet.put("addi", new ADDI());
        instructionSet.put("add", new ADD());
        instructionSet.put("ecall", new ECALL());
        instructionSet.put("li", new LI());
        instructionSet.put("j", new J());
        instructionSet.put("beq", new BEQ());
        instructionSet.put("mv", new MV());
        instructionSet.put("lb", new LB());
        instructionSet.put("jal", new JAL());
        instructionSet.put("and", new AND());
        instructionSet.put("andi", new ANDI());
        instructionSet.put("or", new OR());
        instructionSet.put("ori", new ORI());
        instructionSet.put("xor", new XOR());
        instructionSet.put("xori", new XORI());
        instructionSet.put("not", new NOT());
    }
    public HashMap<String, Instruction> getMap() {
        return instructionSet;
    }
}
