package rars.api;

import rars.Globals;
import rars.javafx.FrontendApi;
import rars.javafx.MainInterface;
import rars.simulator.ProgramStatement;
import rars.simulator.SimulatorThread.SimulationType;

import java.util.ArrayList;
public class TellFrontend {
    public static void inputConsole() {
        FrontendApi.inputConsole();
    }
    public static void outputConsole(String message) {
        if (!Globals.isCompiled) return;
        FrontendApi.updateOutputConsole(message);
    }
    public static void registerUpdate(String name, int value) {
        if (Globals.simulatorThread.getType() == SimulationType.FULL) {
            return;
        }
        FrontendApi.updateRegister(name, value);
    }
    public static void highlightCurrentLine() {
        if (Globals.simulatorThread.getType() == SimulationType.FULL) {
            return;
        }
        int pcValue = Globals.registerTable.get("pc").getValue();
        // TODO: if pc will be memory???
        FrontendApi.highlightInstructionTableRow(pcValue);
    }
    public static void updateInstructionTable(ArrayList<ProgramStatement> statements) {
        FrontendApi.updateInstructionTable(statements);
    }
    public static void resetInstructionTable() {
        FrontendApi.resetInstructionTable();
    }
    public static void stopExecution() {
        FrontendApi.stopExecution();
    }
    public static void updateLogger() {
        FrontendApi.updateLogger();
    }
    public static void resetGui() {
        FrontendApi.reset();
    }
}
