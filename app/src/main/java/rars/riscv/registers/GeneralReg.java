package rars.riscv.registers;

import rars.Globals;
import rars.api.TellFrontend;
import rars.riscv.Register;

/**
 * A simple register that stores integer values
 */
public class GeneralReg implements Register {
    private int value;
    private String name;
    private int number;
    public GeneralReg(String name, int number) {
        this.name = name;
        this.number = number;
        value = 0;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
        TellFrontend.registerUpdate(name, value);
    }
    public String getName() {
        return name;
    }
    public int getNum() {
        return number;
    }
}
