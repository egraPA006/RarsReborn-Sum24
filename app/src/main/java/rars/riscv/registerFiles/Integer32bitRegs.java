package rars.riscv.registerFiles;

import java.util.HashMap;

import rars.riscv.Register;
import rars.riscv.RegisterTable;
import rars.riscv.registers.GeneralReg;
import rars.riscv.registers.PC;
import rars.riscv.registers.ZeroReg;

import java.util.ArrayList;

/**
 * 32 integer registers and program counter
 */
public class Integer32bitRegs implements RegisterTable {
    private HashMap<String, Register> table;
    private ArrayList<String> numbers;
    public Integer32bitRegs() {
        table = new HashMap<>();
        numbers = new ArrayList<>();
        String temp = "zero";
        int number = 0;
        table.put(temp, new ZeroReg());
        numbers.add(temp);
        temp = "ra";
        number = 1;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "sp";
        number = 2;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "gp";
        number = 3;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "tp";
        number = 4;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "t0";
        number = 5;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "t1";
        number = 6;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "t2";
        number = 7;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s0";
        number = 8;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s1";
        number = 9;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "a0";
        number = 10;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "a1";
        number = 11;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "a2";
        number = 12;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "a3";
        number = 13;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "a4";
        number = 14;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "a5";
        number = 15;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "a6";
        number = 16;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "a7";
        number = 17;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s2";
        number = 18;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s3";
        number = 19;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s4";
        number = 20;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s5";
        number = 21;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s6";
        number = 22;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s7";
        number = 23;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s8";
        number = 24;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s9";
        number = 25;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s10";
        number = 26;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "s11";
        number = 27;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "t3";
        number = 28;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "t4";
        number = 29;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "t5";
        number = 30;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        temp = "t6";
        number = 31;
        table.put(temp, new GeneralReg(temp, number));
        numbers.add(temp);
        table.put("pc", new PC());
    }
    public HashMap<String, Register> getMap() {
        return table;
    }
    public ArrayList<String> getArrayList() {
        return numbers;
    }
}
