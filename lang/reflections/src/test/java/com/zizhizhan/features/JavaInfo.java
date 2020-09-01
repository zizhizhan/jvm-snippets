package com.zizhizhan.features;

import java.io.PrintStream;
import java.util.Locale;

public class JavaInfo {

    public static void main(String[] args) {
        PrintStream output = System.out;

        String[] javaProperties = {"os.name", "os.version", "os.arch",
                "user.name", "user.dir", "user.home", "java.vendor",
                "java.version", "java.home", "java.class.path",};
        output.println();

        for (int i = 0; i < javaProperties.length; i++) {
            output.println("System.getProperty(\"" + javaProperties[i]
                    + "\") = '" + System.getProperty(javaProperties[i]) + "'");
        }

        output.println();
        output.println("Runtime.totalMemory() = "
                + Runtime.getRuntime().totalMemory() + " bytes");
        output.println("Runtime.freeMemory()  = "
                + Runtime.getRuntime().freeMemory() + " bytes");
        output.println("used = total - free   = "
                + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                .freeMemory()) + " bytes");
        output.println("Runtime.maxMemory()   = "
                + Runtime.getRuntime().maxMemory() + " bytes");

        Thread current = Thread.currentThread();
        int activeCount = Thread.activeCount();

        output.println();
        output.println("Runtime.availableProcessors() = "
                + Runtime.getRuntime().availableProcessors());
        output.println("Thread.activeCount()          = " + activeCount);

        Thread active[] = new Thread[activeCount];

        Thread.enumerate(active);

        output.println();

        for (int i = 0; i < activeCount; i++) {
            if (active[i] == current) {
                output.print("** ");
            } else {
                output.print("   ");
            }

            output.format(Locale.US, "Thread[%02d] - %s[%08x]: %s", i,
                    active[i].getName(), active[i].hashCode(),
                    active[i].getState());

            int priority = active[i].getPriority();

            switch (priority) {
                case Thread.MIN_PRIORITY:
                    output.print(", MIN_PRIORITY(" + Thread.MIN_PRIORITY + ")");
                    break;
                case Thread.NORM_PRIORITY:
                    output.print(", NORM_PRIORITY(" + Thread.NORM_PRIORITY + ")");
                    break;
                case Thread.MAX_PRIORITY:
                    output.print(", MAX_PRIORITY(" + Thread.MAX_PRIORITY + ")");
                    break;
                default:
                    output.print(", PRIORITY(" + priority + ")");

                    if (priority > Thread.NORM_PRIORITY) {
                        output.print(" > NORM_PRIORITY(" + Thread.NORM_PRIORITY
                                + ")");
                    } else if (priority < Thread.NORM_PRIORITY) {
                        output.print(" < NORM_PRIORITY(" + Thread.NORM_PRIORITY
                                + ")");
                    }

                    break;
            }

            if (active[i] == current) {
                output.print(" **current**");
            }

            output.println();
        }

        output.flush();
    }
}
