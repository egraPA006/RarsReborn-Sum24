package rars.riscv;

/**
 * General interface for register. Maybe it will be changed for some reason, but for now I don't care
 */

 // TODO: Do it generic? How to add floating point?
public interface Register {
    public int getValue();
    public void setValue(int value);
    public String getName();
    public int getNum();
}
