package rars.javafx;


import javafx.application.Platform;
import rars.Globals;
import rars.api.TellBackend;
import rars.simulator.ProgramStatement;

import java.util.ArrayList;
import java.util.List;

public class FrontendApi {
    public static void updateRegister(String name, int value) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().updateRegisterTable(name, value);
                if (!name.equals("pc")) {
                    MainInterface.getInstance().highlightRegisterRow(name);
                }
            }
        });
        
    }
    public static void updateOutputConsole(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().updateOuputConsole(message);
            }
        });
    }
    public static void inputConsole() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().inputConsole();
            }
        });
        
    }
    public static void updateLogger() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().updateLogConsole(Globals.logger.toString());
            }
        });
        
    }
    public static void reset() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().reset();
            }
        });
        
    }
    public static void stopExecution() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().stopExecution();
            }
        });
        
    }
    public static void updateInstructionTable(ArrayList<ProgramStatement> statements) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().updateInstructionTable(statements);
            }
        });
        
    }
    public static void resetInstructionTable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().resetInstructionTable();
            }
        });
        
    }
    public static void highlightInstructionTableRow(int value) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainInterface.getInstance().highlightInstructionTableRow(value);
            }
        });
        
    }
}