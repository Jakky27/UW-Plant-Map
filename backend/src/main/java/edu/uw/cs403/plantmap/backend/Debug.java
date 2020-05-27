package edu.uw.cs403.plantmap.backend;

public class Debug {
    private static boolean IS_ENABLED = false;

    private Debug() { }

    public static void enable() {
        IS_ENABLED = true;
    }

    public static String getCallerFunctionTag() {
        if (!IS_ENABLED)
            return "unknown";

        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[3];
        return e.getClassName() + "/" + e.getMethodName();
    }

    public static void print(String out) {
        if (IS_ENABLED)
            System.out.println("DEBUG/" + getCallerFunctionTag() + ": " + out);
    }
}
