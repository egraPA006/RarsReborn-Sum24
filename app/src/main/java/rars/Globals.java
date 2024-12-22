package rars;

import rars.riscv.*;
import rars.riscv.instructionSets.InstructionInitialSet;
import rars.riscv.registerFiles.Integer32bitRegs;
import rars.riscv.sysCallTables.InitialSysCalls;
import rars.simulator.RiscvProgram;
import rars.simulator.Simulator;
import rars.simulator.SimulatorThread;
import rars.simulator.SimulatorThread.SimulationType;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.lang.*;

public class Globals {
    public static final int simulationDelay = 10;
    public static HashMap <String, Register> registerTable = new HashMap<>();
    public static HashMap <String, Instruction> instructionMap = new HashMap<>();
    public static HashMap <Integer, SysCall> sysCallTable = new HashMap<>();
    public static File assembleFile;
    public static boolean isCompiled = false;
    public static RiscvProgram riscvProgram;
    public static Simulator simulator;
    public static Scanner scanner = new Scanner(System.in);
    public static SysCall currentSysCall;
    public static String consoleMessage;
    public static SimulatorThread simulatorThread;
    public static Logger logger = new Logger();
    // TODO: extensions to separate file?
    private static void initInstructionsSets() {
        // TODO: Create instructions set config
        // Prepare.
        try {
            String packageName = "rars.riscv.instructionSets";
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URI root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".",  File.separator)).toURI();
            Path myPath;
            if (root.getScheme().equals("jar")) {
                FileSystem fileSystem;
                try {
                    fileSystem = FileSystems.getFileSystem(root);
                }
                catch (FileSystemNotFoundException e) {
                    fileSystem = FileSystems.newFileSystem(root, Collections.<String, Object>emptyMap());
                }
                
                myPath = fileSystem.getPath(packageName.replace(".", File.separator));
              
            } else {
                myPath = Paths.get(root);
            }
            // Filter .class files.
            Stream<Path> walk = Files.walk(myPath, 1);
            List<String> files = new ArrayList<>();
            for (Iterator<Path> it = walk.iterator(); it.hasNext();){
                Path cur = it.next();
                if (cur.toString().endsWith(".class")) {
                    files.add(cur.getFileName().toString().replaceAll(".class$", ""));
                }
            }
            // Find classes implementing ICommand.
            for (String file : files) {
                Class<?> cls = cl.loadClass(packageName + "." + file);
                if (InstructionTable.class.isAssignableFrom(cls)) {
                    HashMap <String, Instruction> table = ((Class<InstructionTable>) cls).getDeclaredConstructor().newInstance().getMap();
                    instructionMap.putAll(table);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    // TODO: Create registers config
    private static void initRegistersSets() {
        // Prepare.
        try {
            String packageName = "rars.riscv.registerFiles";
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URI root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".",  File.separator)).toURI();
            Path myPath;
            if (root.getScheme().equals("jar")) {
                FileSystem fileSystem;
                try {
                    fileSystem = FileSystems.getFileSystem(root);
                }
                catch (FileSystemNotFoundException e) {
                    fileSystem = FileSystems.newFileSystem(root, Collections.<String, Object>emptyMap());
                }
                
                myPath = fileSystem.getPath(packageName.replace(".", File.separator));
                  
            } else {
                myPath = Paths.get(root);
            }
            // Filter .class files.
            Stream<Path> walk = Files.walk(myPath, 1);
            List<String> files = new ArrayList<>();
            for (Iterator<Path> it = walk.iterator(); it.hasNext();){
                Path cur = it.next();
                if (cur.toString().endsWith(".class")) {
                    files.add(cur.getFileName().toString().replaceAll(".class$", ""));
                }
            }
            // Find classes implementing ICommand.
            for (String file : files) {
                Class<?> cls = cl.loadClass(packageName + "." + file);
                if (RegisterTable.class.isAssignableFrom(cls)) {
                    HashMap <String, Register> table = ((Class<RegisterTable>) cls).getDeclaredConstructor().newInstance().getMap();
                    registerTable.putAll(table);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    private static void initSysCalls() {
        // Prepare.
        try {
            String packageName = "rars.riscv.sysCallTables";
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URI root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".",  File.separator)).toURI();
            Path myPath;
            
            if (root.getScheme().equals("jar")) {
                FileSystem fileSystem;
                try {
                    fileSystem = FileSystems.getFileSystem(root);
                }
                catch (FileSystemNotFoundException e) {
                    fileSystem = FileSystems.newFileSystem(root, Collections.<String, Object>emptyMap());
                }
                
                myPath = fileSystem.getPath(packageName.replace(".", File.separator));
            
            } else {
                myPath = Paths.get(root);
            }
            // Filter .class files.
            Stream<Path> walk = Files.walk(myPath, 1);
            List<String> files = new ArrayList<>();
            for (Iterator<Path> it = walk.iterator(); it.hasNext();){
                Path cur = it.next();
                if (cur.toString().endsWith(".class")) {
                    files.add(cur.getFileName().toString().replaceAll(".class$", ""));
                }
            }
            // Find classes implementing.
            for (String file : files) {
                Class<?> cls = cl.loadClass(packageName + "." + file);
                if (SyscallTable.class.isAssignableFrom(cls)) {
                    HashMap <Integer, SysCall> table = ((Class<SyscallTable>) cls).getDeclaredConstructor().newInstance().getMap();
                    sysCallTable.putAll(table);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public static void initialize() {
        initInstructionsSets();
        initRegistersSets();
        initSysCalls();
    }
    public static void reset() {
        simulatorThread = new SimulatorThread(SimulationType.IDLE);
        currentSysCall = null;
        for (String name : registerTable.keySet()) {
            registerTable.get(name).setValue(0);
        }
    }
}