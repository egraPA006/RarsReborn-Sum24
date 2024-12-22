package rars.riscv.devices;

import java.util.ArrayList;

import javafx.stage.FileChooser.ExtensionFilter;

public class Memory {
    private int memorySize = 10000;
    private final int instructionAddress = 0;
    private final int dataAddress = 5000;
    private final int instructionSize = 4;
    private final int wordSize = 4;
    ArrayList<Integer> data = null;
    public Memory(int size) {
        memorySize = size;
        data = new ArrayList<>(memorySize);
    } 
    public Memory() {
        data = new ArrayList<>(memorySize);
    }
    public int getInstruction(int address) throws Exception {
        if (address < instructionAddress || address + instructionAddress >= dataAddress) {
            throw new Exception("Segmentation fault");
        }
        int ans = 0;
        int pow = 1;
        for (int i = 0; i < instructionSize; i++) {
            ans += data.get(address + (instructionSize - i)) * pow;
            pow *= 256;
        }
        return ans;
    }
    public int getByte(int address) throws Exception {
        if (address < dataAddress || address >= memorySize) {
            throw new Exception("Segmentation fault");
        }
        return data.get(address);
    }
    public int getWord(int address) throws Exception {
        if (address < dataAddress || address + instructionSize > memorySize) {
            throw new Exception("Segmentation fault");
        }
        int ans = 0;
        int pow = 1;
        for (int i = 0; i < instructionSize; i++) {
            ans += data.get(address + (instructionSize - i)) * pow;
            pow *= 256;
        }
        return ans;
    }
    // TODO: half word
    // TODO: long words should be implemented
}
