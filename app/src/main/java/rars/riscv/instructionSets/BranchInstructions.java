package rars.riscv.instructionSets;

import java.util.HashMap;
import rars.riscv.instructions.*;
import rars.riscv.Instruction;
import rars.riscv.InstructionTable;

/*
 * Simple instruction set for mvp. Will be extended to RV32I
 */
public class BranchInstructions implements InstructionTable {
    private HashMap<String, Instruction> instructionSet;
    public BranchInstructions() {
        instructionSet = new HashMap<>();
        instructionSet.put("bge", new BGE());
        instructionSet.put("blt", new BLT());
        instructionSet.put("bne", new BNE());
        instructionSet.put("bgt", new BGT());
        instructionSet.put("ble", new BLE());
    }
    public HashMap<String, Instruction> getMap() {
        return instructionSet;
    }
}
