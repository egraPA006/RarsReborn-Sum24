package rars;

import rars.assembler.Assembler;
import rars.assembler.Token;
import rars.assembler.Tokenizer;
import rars.javafx.MainInterface;
import rars.simulator.RiscvProgram;
import rars.simulator.Simulator;
import rars.simulator.SourceLine;

import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;

public class Launch {
    public static void main(String[] args) {
        Globals.initialize();
        Application.launch(MainInterface.class);
        Globals.scanner.close();
    }
}