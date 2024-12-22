package rars.riscv.syscalls;

import java.util.Scanner;

import rars.Globals;
import rars.api.TellFrontend;
import rars.riscv.SysCall;

/**
 * For now simple integer input to a0 register from console
 */
public class ReadIntSys implements SysCall {

    @Override
    public void execute() {
        if (Globals.currentSysCall == this) {
            Scanner s = new Scanner(Globals.consoleMessage);
            if (!s.hasNextInt()) {
                System.out.println("Error: Wrong input");
                return;
            }
            int a = s.nextInt();
            if (s.hasNextInt()) {
                System.out.println("Error: Additional input found");
                return;
            }
            Globals.registerTable.get("a0").setValue(a);
            System.out.println("Received input: " + Globals.consoleMessage);
            Globals.currentSysCall = null;
        } else {
            Globals.currentSysCall = this;
            TellFrontend.inputConsole();
        }
    }
}