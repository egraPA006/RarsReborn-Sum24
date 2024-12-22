package rars.riscv;

import java.util.List;
import rars.assembler.Token;

/**
 *  (◕‿◕)
 * Hi there! I am Interface for any instruction! Nice to meet you! How did u end up in this deep shitty java code?
 */
public interface Instruction {
    public boolean checkOperands(List<Token> args);
}