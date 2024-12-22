package rars.simulator;

import rars.Globals;
import rars.api.TellFrontend;

public class SimulatorThread extends Thread {
    public enum SimulationType {
        LINE,
        FULL,
        DEBUG,
        IDLE
    }
    private SimulationType type;
    public SimulatorThread(SimulationType type) {
        this.type = type;
    }
    public SimulationType getType() {
        return type;
    }
    public void run() {
        switch (type) {
            case LINE:
                if (Globals.currentSysCall != null) {
                    Globals.logger.warn("Syscall in progress, cannot continue execution");
                    return;
                }
                if (!Globals.simulator.simulateLine()) {
                    Globals.isCompiled = false;
                    Globals.logger.info("Finished simulation");
                    TellFrontend.stopExecution();
                } 
                break;
            case FULL:
                while (true) {
                    try {
                        sleep(Globals.simulationDelay);
                    } catch (InterruptedException e) {
                    }
                    
                    if (Globals.currentSysCall != null) {
                        continue;
                    }
                    if (!Globals.simulator.simulateLine()) {
                        Globals.isCompiled = false;
                        Globals.logger.info("Finished simulation");
                        TellFrontend.stopExecution();
                        break;
                    }
                }
                break;
            default:
                Globals.logger.warn("Simulation type not specified, cannot simulate, exiting...");
        }
    }
}
