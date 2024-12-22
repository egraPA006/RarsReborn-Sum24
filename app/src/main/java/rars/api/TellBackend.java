package rars.api;

import rars.Globals;
import rars.Logger;
import rars.assembler.Assembler;
import rars.simulator.ProgramStatement;
import rars.simulator.RiscvProgram;
import rars.simulator.Simulator;
import rars.simulator.SimulatorThread;
import rars.simulator.SimulatorThread.SimulationType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;

public class TellBackend {
    public static void runFullProgram() {
        if (Globals.isCompiled) {
            Globals.simulatorThread.interrupt();
            Globals.isCompiled = false;
            Globals.logger.info("Stopped simulation");
            TellFrontend.stopExecution();
            return;
        }
        assemble();
        if (!Globals.isCompiled) {
            TellFrontend.stopExecution();
            return;
        }
        try {
            Globals.simulatorThread = new SimulatorThread(SimulationType.FULL);
            Globals.simulatorThread.setDaemon(true);
            Globals.simulatorThread.start();
        } catch (Exception e) {
            Globals.logger.error(e.getMessage());
            Globals.isCompiled = false;
            return;
        }
    }
    public static void updateMessage() {
        
    }
    public static void runLine() {
        if (!Globals.isCompiled) {
            assemble();
        }
        if (!Globals.isCompiled) {
            TellFrontend.stopExecution();
            return;
        }
        try {
            Globals.simulatorThread = new SimulatorThread(SimulationType.LINE);
            Globals.simulatorThread.setDaemon(true);
            Globals.simulatorThread.start();
        } catch (Exception e) {
            Globals.logger.error(e.getMessage());
            Globals.isCompiled = false;
            return;
        }      
    }
    public static void runBreakPoint() {

    }
    public static void stepBack() {

    }
    public static void inputConsole(String message) {
        if (Globals.currentSysCall == null) {
            Globals.logger.debug("Error: No current syscall");
            return;
        }
        Globals.consoleMessage = message;
        Globals.currentSysCall.execute();
    }
    private static void assemble() {
        File file = Globals.assembleFile;
        ArrayList<String> a = new ArrayList<>();
        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                a.add(s.nextLine());
            }
            s.close();
        } catch (Exception e) {
            Globals.logger.error("Could not read from file, exiting...");
            return;
        }
        Globals.riscvProgram = new RiscvProgram(a, file.getName());
        try {
            Assembler assembler = new Assembler();
            assembler.assemble();
        } catch (Exception e) {
            Globals.logger.error(e.getMessage());
            return;
        }
        TellFrontend.resetGui();
        TellFrontend.resetInstructionTable();
        TellFrontend.updateInstructionTable(Globals.riscvProgram.basicCode);
        Globals.reset();
        Globals.simulator = new Simulator();
        Globals.isCompiled = true;
        Globals.logger.info("Finished assemble");
    }
}