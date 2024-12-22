package rars.riscv;

import java.util.HashMap;

public interface InstructionTable {
    public HashMap<String, Instruction> getMap();
}
