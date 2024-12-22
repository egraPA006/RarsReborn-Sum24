package rars.riscv.instructions;
import javafx.application.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import rars.Globals;
import rars.api.TellFrontend;
import rars.assembler.Assembler;
import rars.assembler.Preprocessor;
import rars.javafx.MainInterface;
import rars.simulator.RiscvProgram;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
class ANDITest {
    MockedStatic<TellFrontend> mocked;

    @BeforeEach
    void setUp () {
        Globals.initialize();
        mocked = Mockito.mockStatic(TellFrontend.class);
    }
    @AfterEach
    void closeMoke() {
        mocked.close();
    }
    @Test
    void fifteenAndFourtyTwoEq10 () {
        ArrayList<String> sourceCode = new ArrayList<>() ;
        sourceCode.add("addi t1 t1 42");
        sourceCode.add("andi t3 t1 15");

        RiscvProgram p = new RiscvProgram(sourceCode,"ff");
        p.preprocessedSourceCode = Preprocessor.process(p.sourceCode, p.sourceFile);
        Globals.riscvProgram = p;
        try {
            Assembler assembler = new Assembler();
            assembler.assemble();
        } catch (Exception e) {

        }
        ANDI andi = new ANDI();
        ADDI addi = new ADDI();
        addi.simulate(p.basicCode.get(0).operands);
        andi.simulate(p.basicCode.get(1).operands);


        assertEquals(10, Globals.registerTable.get("t3").getValue());
    }
}