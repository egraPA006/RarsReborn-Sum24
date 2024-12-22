package rars.riscv.instructions;
import rars.riscv.*;
import rars.simulator.Operand;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import rars.Globals;
import rars.assembler.Token;
import rars.assembler.TokenType;
public class ECALL implements BasicInstruction {
    public boolean checkOperands(List<Token> args) {
        if (args.size() == 0) return true;
        return false;
    }
    public void simulate(ArrayList<Operand> args){
        int a = Globals.registerTable.get("a7").getValue();
        if (Globals.sysCallTable.containsKey(a)) {
            Globals.sysCallTable.get(a).execute(); // FIXME: error????
        }
    }
}