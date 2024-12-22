package rars.simulator;

public class LabelOperand implements Operand {
    private String value;
    public LabelOperand(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}