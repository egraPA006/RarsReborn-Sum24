package rars.simulator;

/**
 * A handy class that represents a line substituted to source code
 * during preprocess
 * @see rars.assembler.Assembler
 */
public class SourceLine {
    public String value;
    public String sourceFile;
    public int sourceFileLine;
    public SourceLine(String value, String sourceFile, int line) {
        this.value = value;
        this.sourceFile = sourceFile;
        this.sourceFileLine = line;
    }
}
