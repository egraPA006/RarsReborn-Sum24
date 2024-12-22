package rars.riscv.syscalls;

import rars.Globals;
import rars.api.TellFrontend;
import rars.riscv.SysCall;

/**
 * For now simple integer output from a0 register to console
 */
public class WriteIntSys implements SysCall {
    public void execute() {
        int a = Globals.registerTable.get("a0").getValue();
        Globals.currentSysCall = null;
        TellFrontend.outputConsole(Integer.toString(a));
    }
}
