package rars.javafx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RegisterData {
    private final SimpleIntegerProperty number;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty value;

    public RegisterData(String name, int value, int number) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleIntegerProperty(value);
        this.number = new SimpleIntegerProperty(number);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getValue() {
        return value.get();
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public int getNumber() {
        return number.get();
    }

    public void setNumber(int number) {
        this.number.set(number);
    }
}
