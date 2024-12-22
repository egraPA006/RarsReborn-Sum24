package rars.simulator;


import rars.assembler.Token;

import java.util.ArrayList;
/**
 * RiscvProgram
 * 
 */
public class RiscvProgram {
    /**
     * Initial source code of the file that is executed
     */ 
    public ArrayList<String> sourceCode;
    /**
     * Array of program statements that is ready to be simulated
     */
    public ArrayList<ProgramStatement> basicCode;

    public ArrayList<ProgramStatement> pseudoBasicCode;
    /**
     * Source code with includes and macros substituted
     */
    public ArrayList<SourceLine> preprocessedSourceCode;
    public ArrayList<ArrayList<Token>> preprocessedTokenisedCode;
    public String sourceFile;
    public RiscvProgram(ArrayList<String> sourceCode, String sourceFile) {
        this.sourceFile = sourceFile;
        this.sourceCode = sourceCode;
        basicCode = new ArrayList<>();
        preprocessedSourceCode = new ArrayList<>();
        preprocessedTokenisedCode = new ArrayList<>();
        pseudoBasicCode = new ArrayList<>();
    }
}