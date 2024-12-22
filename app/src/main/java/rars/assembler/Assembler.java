package rars.assembler;
import rars.simulator.ImOperand;
import rars.simulator.LabelOperand;
import rars.simulator.Operand;
import rars.simulator.ProgramStatement;
import rars.simulator.RegOperand;
import rars.simulator.RiscvProgram;
import rars.riscv.*;
import java.util.ArrayList;
import rars.Globals;
import java.util.HashMap;
public class Assembler {
    private RiscvProgram riscvProgram;
    public Assembler() {
        riscvProgram = Globals.riscvProgram;
    }
    public void assemble() throws Exception {
        if (riscvProgram.sourceCode.size() == 0) {
            return; // There is nothing to do!
        }
        riscvProgram.preprocessedSourceCode = Preprocessor.process(riscvProgram.sourceCode, riscvProgram.sourceFile);
        Tokenizer tokenizer = new Tokenizer(riscvProgram);
        tokenizer.tokenize();
        ProgramStatement newStatement = new ProgramStatement();
        // create program statements, with pseudo instructions.
        for (int i = 0; i < riscvProgram.preprocessedSourceCode.size(); i++) {
            int j = 0;
            ArrayList<Token> tokenLine = riscvProgram.preprocessedTokenisedCode.get(i);
            if (tokenLine.size() == 0) {
                // nothing to do;
                continue;
            }
            // Check for label:
            if (tokenLine.get(j).getType() == TokenType.LABEL_INIT) {
                newStatement.label = tokenLine.get(j).getValue();
                if (tokenLine.size() == 1) continue; // if only label
                j++;
            }
            // Now there should be an instruction
            Instruction instruction = matchInstruction(tokenLine.get(j).getValue());
            if (instruction == null) {
                throw new Exception("Failed to assembly line " + riscvProgram.preprocessedSourceCode.get(i).sourceFileLine
                 + " of file " + riscvProgram.preprocessedSourceCode.get(i).sourceFile + ": undefined instruction '" + tokenLine.get(j).getValue() + "'");
            }
            if (!instruction.checkOperands(tokenLine.subList(j + 1, tokenLine.size()))) {
                throw new Exception("Failed to assembly line " + riscvProgram.preprocessedSourceCode.get(i).sourceFileLine
                 + " of file " + riscvProgram.preprocessedSourceCode.get(i).sourceFile + ": wrong operands for instruction '" + instruction.getClass().toString().toLowerCase() + "'");
            }
            newStatement.instruction = instruction;
            j++;
            
            // Now there should be operands. Let us parse them and check if they match the instruction
            ArrayList<Operand> operands = new ArrayList<>();
            Operand op;
            while (j < tokenLine.size()) {
                if (tokenLine.get(j).getType().equals(TokenType.REGISTER)) {
                    op = new RegOperand(matchRegister(tokenLine.get(j).getValue()));
                } else if (tokenLine.get(j).getType().equals(TokenType.CONSTANT)) { // FIXME: Mb Constant???
                    op = new ImOperand(Integer.parseInt(tokenLine.get(j).getValue()));
                } else if (tokenLine.get(j).getType().equals(TokenType.LABEL)) {
                    op = new LabelOperand(tokenLine.get(j).getValue());
                } else {
                    throw new Exception("Failed to assembly line " + riscvProgram.preprocessedSourceCode.get(i).sourceFileLine
                 + " of file " + riscvProgram.preprocessedSourceCode.get(i).sourceFile + ": unknown token type");
                }
                operands.add(op);
                j++;
            }
            newStatement.operands = operands;
            newStatement.sourceLine = riscvProgram.preprocessedSourceCode.get(i);
            riscvProgram.pseudoBasicCode.add(newStatement);
            newStatement = new ProgramStatement();
        }
        // Now we can extend pseudo instructions into basic instructions
        HashMap<String, Integer> labelTable = new HashMap<>();
        for (int i = 0; i < riscvProgram.pseudoBasicCode.size(); i++) {
            ProgramStatement statement = riscvProgram.pseudoBasicCode.get(i);
            if (statement.instruction instanceof PseudoInstruction) {
                ArrayList<ProgramStatement> basics = ((PseudoInstruction)statement.instruction).template(statement.operands);
                for (ProgramStatement basic : basics) {
                    basic.sourceLine = statement.sourceLine;
                }
                basics.get(0).label = statement.label;
                riscvProgram.basicCode.addAll(basics);
            } else {
                statement.basicLine = statement.sourceLine.value;
                riscvProgram.basicCode.add(statement);
            }
            if (statement.label != null) {
                labelTable.put(statement.label.substring(0, statement.label.length() - 1), riscvProgram.basicCode.size() - 1); // FIXME: semicolon -_-????
            }
        }
        // Now we only should solve the labels and convert everything to machine code ( TODO: machine code)
        // TODO: also consider creating basicCode again without labels
        for (int i = 0; i < riscvProgram.basicCode.size(); i++) {
            ProgramStatement statement = riscvProgram.basicCode.get(i);
            for (int j = 0; j < statement.operands.size(); j++) {
                Operand operand = statement.operands.get(j);
                if (operand instanceof LabelOperand labelOperand) {
                    if (labelTable.containsKey(labelOperand.getValue())) {
                        ImOperand imOperand = new ImOperand(labelTable.get(labelOperand.getValue()) - i - 1);
                        statement.operands.set(j, imOperand);
                    } else {
                        throw new Exception("Failed to assembly line " + statement.sourceLine.sourceFileLine + " of file " + statement.sourceLine.sourceFile + ": invalid label found");
                    }
                }
                
            }
            statement.memoryAddress = i;
            Globals.logger.debug("Assembled line" + statement.sourceLine.sourceFileLine + " of file " + statement.sourceLine.sourceFile +
             ": " + statement.basicLine);
        }
    }
    private Instruction matchInstruction(String value) {
        return Globals.instructionMap.get(value);
    }
    private Register matchRegister(String value) {
        return Globals.registerTable.get(value);
    }
}
