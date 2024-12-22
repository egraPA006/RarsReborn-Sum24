package rars.simulator;

import rars.riscv.Register;

public class RegOperand implements Operand {
    private Register register;
    public RegOperand(Register value) {
        this.register = value;
    }
    public Register getValue() {
        return register;
    }
}
