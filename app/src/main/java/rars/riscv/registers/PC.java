package rars.riscv.registers;
import rars.Globals;
import rars.api.TellBackend;
import rars.api.TellFrontend;
import rars.riscv.Register;

/**
 * Simple implementation of program counter
 */
public class PC implements Register {
    private int value;
    private String name;
    private int number;
    public PC() {
        name = "pc";
        number = 0;
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

    public void increment(int value) {
        this.value += value;
        TellFrontend.registerUpdate(name, this.value);
    }
}