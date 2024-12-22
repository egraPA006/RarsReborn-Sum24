package rars.riscv.sysCallTables;

import rars.riscv.SysCall;
import rars.riscv.SyscallTable;
import rars.riscv.syscalls.ReadIntSys;
import rars.riscv.syscalls.WriteIntSys;

import java.util.HashMap;

public class InitialSysCalls implements SyscallTable {
    HashMap<Integer, SysCall> table;
    public InitialSysCalls() {
        table = new HashMap<>();
        table.put(5, new ReadIntSys());
        table.put(1, new WriteIntSys());
    }
    public HashMap<Integer, SysCall> getMap() {
        return table;
    }
}
