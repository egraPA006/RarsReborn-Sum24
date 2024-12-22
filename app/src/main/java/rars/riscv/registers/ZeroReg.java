package rars.riscv.registers;
import rars.riscv.Register;

/**
 * Hardwired ZERO register (Cannot set value to anything)
 */
public class ZeroReg implements Register {
    private final int value;
    private String name;
    private int number;
    public ZeroReg() {
        this.name = "zero";
        this.number = 0;
        value = 0;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
    }
    public String getName() {
        return name;
    }
    public int getNum() {
        return number;
    }
}
    