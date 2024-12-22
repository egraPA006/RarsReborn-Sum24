package rars.javafx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InstructionData {
    private SimpleStringProperty address;
    private SimpleStringProperty machineCode;
    private SimpleStringProperty basic;
    private SimpleStringProperty real;
    private SimpleStringProperty source;

    public InstructionData(String address, String machineCode, String basic, String real, String source) {
        this.address = new SimpleStringProperty(address);
        this.machineCode = new SimpleStringProperty(machineCode);
        this.basic = new SimpleStringProperty(basic);
        this.real = new SimpleStringProperty(real);
        this.source = new SimpleStringProperty(source);
    }
    public InstructionData() {}

    public void setAddress(String address) {
        this.address = new SimpleStringProperty(address);
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = new SimpleStringProperty(machineCode);
    }

    public void setBasic(String basic) {
        this.basic = new SimpleStringProperty(basic);
    }

    public void setReal(String real) {
        this.real = new SimpleStringProperty(real);
    }

    public void setSource(String source) {
        this.source = new SimpleStringProperty(source);
    }
    public SimpleStringProperty getAddress() {
        return address;
    }
    public SimpleStringProperty getMachineCode() {
        return machineCode;
    }
    public SimpleStringProperty getBasic() {
        return basic;
    }
    public SimpleStringProperty getReal() {
        return real;
    }
    public SimpleStringProperty getSource() {
        return source;
    }
}
