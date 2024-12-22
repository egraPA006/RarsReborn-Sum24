package rars.riscv;

import java.util.HashMap;


public interface SyscallTable {
    public HashMap<Integer, SysCall> getMap();
}
