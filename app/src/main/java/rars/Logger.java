package rars;
import rars.api.TellFrontend;
import java.util.Queue;
import java.util.PriorityQueue;

public class Logger {
    private Queue<String> messages;
    private Queue<Type> types;
    public boolean debugMode = false;
    public enum Type {
        INFO,
        DEBUG,
        ERROR,
        WARN
        ;
        public String toString() {
            return this.name().toLowerCase();
        }
    }
    public Logger() {
        messages = new PriorityQueue<>();
        types = new PriorityQueue<>();
    }
    public void info(String message) {
        set(Type.INFO, message);
    }
    public void debug(String message) {
        if (debugMode) {
            set(Type.DEBUG, message);
        }
    }
    public void error(String message) {
        set(Type.ERROR, message);
    }
    public void warn(String message) {
        set(Type.WARN, message);
    }
    public void set(Type type, String text) {
        this.types.add(type);
        this.messages.add(text);
        TellFrontend.updateLogger();
    }
    public String toString() {
        return "[" + types.poll().toString() + "] " + messages.poll();
    }
}