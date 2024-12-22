package rars.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rars.Globals;
import rars.assembler.Preprocessor;
import rars.assembler.Token;
import rars.assembler.TokenType;
import rars.assembler.Tokenizer;
import rars.simulator.RiscvProgram;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {
    @BeforeEach
    void setUp() {
        Globals.initialize();

    }
    @Test
    void testTokenizer() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("li a7 0");
        sourceCode.add ("li a0 12");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        // first line
        assertEquals(t.get(0).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(0).get(0).getValue(), "li");
        assertEquals(t.get(0).get(1).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(1).getValue(), "a7");
        assertEquals(t.get(0).get(2).getType(), TokenType.CONSTANT);
        assertEquals(t.get(0).get(2).getValue(), "0");
        assertEquals(t.get(0).size(), 3);
        // second line
        assertEquals(t.get(1).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(1).get(0).getValue(), "li");
        assertEquals(t.get(1).get(1).getType(), TokenType.REGISTER);
        assertEquals(t.get(1).get(1).getValue(), "a0");
        assertEquals(t.get(1).get(2).getType(), TokenType.CONSTANT);
        assertEquals(t.get(1).get(2).getValue(), "12");
        assertEquals(t.get(1).size(), 3);
    }
    @Test
    void testLabelInitDetection() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("a: li a7 0");
        sourceCode.add ("li a0 12");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        // first line
        assertEquals(t.get(0).get(0).getType(), TokenType.LABEL_INIT);
        assertEquals(t.get(0).get(0).getValue(), "a:");
        assertEquals(t.get(0).get(1).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(0).get(1).getValue(), "li");
        assertEquals(t.get(0).get(2).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(2).getValue(), "a7");
        assertEquals(t.get(0).get(3).getType(), TokenType.CONSTANT);
        assertEquals(t.get(0).get(3).getValue(), "0");
        assertEquals(t.get(0).size(), 4);
        // second line
        assertEquals(t.get(1).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(1).get(0).getValue(), "li");
        assertEquals(t.get(1).get(1).getType(), TokenType.REGISTER);
        assertEquals(t.get(1).get(1).getValue(), "a0");
        assertEquals(t.get(1).get(2).getType(), TokenType.CONSTANT);
        assertEquals(t.get(1).get(2).getValue(), "12");
        assertEquals(t.get(1).size(), 3);
    }
    @Test
    void testDirectiveDetection() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add(".a li a7 0");
        sourceCode.add ("li a0 12");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        // first line
        assertEquals(t.get(0).get(0).getType(), TokenType.DIRECTIVE);
        assertEquals(t.get(0).get(0).getValue(), ".a");
        assertEquals(t.get(0).get(1).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(0).get(1).getValue(), "li");
        assertEquals(t.get(0).get(2).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(2).getValue(), "a7");
        assertEquals(t.get(0).get(3).getType(), TokenType.CONSTANT);
        assertEquals(t.get(0).get(3).getValue(), "0");
        assertEquals(t.get(0).size(), 4);
        // second line
        assertEquals(t.get(1).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(1).get(0).getValue(), "li");
        assertEquals(t.get(1).get(1).getType(), TokenType.REGISTER);
        assertEquals(t.get(1).get(1).getValue(), "a0");
        assertEquals(t.get(1).get(2).getType(), TokenType.CONSTANT);
        assertEquals(t.get(1).get(2).getValue(), "12");
        assertEquals(t.get(1).size(), 3);
    }


    @Test
    void TestComments1() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("lb t1 10 (t2)#aziz");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        assertEquals(t.get(0).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(0).get(0).getValue(), "lb");

        assertEquals(t.get(0).size(), 6);

        assertEquals(t.get(0).get(1).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(1).getValue(), "t1");

        assertEquals(t.get(0).get(2).getType(), TokenType.CONSTANT);
        assertEquals(t.get(0).get(2).getValue(), "10");

        assertEquals(t.get(0).get(3).getType(), TokenType.OPEN_BRACKET);
        assertEquals(t.get(0).get(3).getValue(), "(");

        assertEquals(t.get(0).get(4).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(4).getValue(), "t2");

        assertEquals(t.get(0).get(5).getType(), TokenType.CLOSE_BRACKET);
        assertEquals(t.get(0).get(5).getValue(), ")");
    }
    @Test
    void TestComments2() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("li t1 10          #aziz");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        assertEquals(t.get(0).get(0).getValue(),"li");
        assertEquals(t.get(0).get(0).getType(),TokenType.INSTRUCTION);

        assertEquals(t.get(0).get(1).getValue(),"t1");
        assertEquals(t.get(0).get(1).getType(),TokenType.REGISTER);

        assertEquals(t.get(0).get(2).getValue(),"10");
        assertEquals(t.get(0).get(2).getType(),TokenType.CONSTANT);

        assertEquals(t.get(0).size(), 3);
    }
    @Test
    void TestComments3() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("#li t1 10          #aziz");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        assertEquals(t.size(),1);
    }
    @Test

    void testOffsetDetection1() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("lb t1 10 (t2)");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        // first line
        assertEquals(t.get(0).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(0).get(0).getValue(), "lb");

        assertEquals(t.get(0).size(), 6);

        assertEquals(t.get(0).get(1).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(1).getValue(), "t1");

        assertEquals(t.get(0).get(2).getType(), TokenType.CONSTANT);
        assertEquals(t.get(0).get(2).getValue(), "10");

        assertEquals(t.get(0).get(3).getType(), TokenType.OPEN_BRACKET);
        assertEquals(t.get(0).get(3).getValue(), "(");

        assertEquals(t.get(0).get(4).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(4).getValue(), "t2");

        assertEquals(t.get(0).get(5).getType(), TokenType.CLOSE_BRACKET);
        assertEquals(t.get(0).get(5).getValue(), ")");

    }


    @Test

    void testOffsetDetection2() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("lb t1 10(t2)");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        // first line
        assertEquals(t.get(0).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(0).get(0).getValue(), "lb");

        assertEquals(t.get(0).size(), 6);

        assertEquals(t.get(0).get(1).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(1).getValue(), "t1");

        assertEquals(t.get(0).get(2).getType(), TokenType.CONSTANT);
        assertEquals(t.get(0).get(2).getValue(), "10");

        assertEquals(t.get(0).get(3).getType(), TokenType.OPEN_BRACKET);
        assertEquals(t.get(0).get(3).getValue(), "(");

        assertEquals(t.get(0).get(4).getType(), TokenType.REGISTER);
        assertEquals(t.get(0).get(4).getValue(), "t2");

        assertEquals(t.get(0).get(5).getType(), TokenType.CLOSE_BRACKET);
        assertEquals(t.get(0).get(5).getValue(), ")");

    }
    @Test
    void testLabelDetection() {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("j loop");
        sourceCode.add ("li a0 12");
        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Tokenizer tokenizer = new Tokenizer(p);
        tokenizer.tokenize();
        ArrayList<ArrayList<Token>> t = p.preprocessedTokenisedCode;
        // first line
        assertEquals(t.get(0).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(0).get(0).getValue(), "j");
        assertEquals(t.get(0).get(1).getType(), TokenType.LABEL);
        assertEquals(t.get(0).get(1).getValue(), "loop");
        assertEquals(t.get(0).size(), 2);
        // second line
        assertEquals(t.get(1).get(0).getType(), TokenType.INSTRUCTION);
        assertEquals(t.get(1).get(0).getValue(), "li");
        assertEquals(t.get(1).get(1).getType(), TokenType.REGISTER);
        assertEquals(t.get(1).get(1).getValue(), "a0");
        assertEquals(t.get(1).get(2).getType(), TokenType.CONSTANT);
        assertEquals(t.get(1).get(2).getValue(), "12");
        assertEquals(t.get(0).size(), 2);
        assertEquals(t.get(1).size(), 3);
    }

}