package rars.simulator;

/**
 * 
 */
public class ImOperand implements Operand {
    private int value;
    public ImOperand(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
