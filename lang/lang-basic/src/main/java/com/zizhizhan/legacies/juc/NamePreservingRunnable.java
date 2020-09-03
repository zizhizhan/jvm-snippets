package com.zizhizhan.legacies.juc;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class NamePreservingRunnable implements Runnable {
    private final String newName;
    private final Runnable runnable;

    public NamePreservingRunnable(Runnable runnable, String newName) {
        this.runnable = runnable;
        this.newName = newName;
    }

    public void run() {
        final Thread currentThread = Thread.currentThread();
        final String oldName = currentThread.getName();

        if (newName != null) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    currentThread.setName(newName);
                    return null;
                }
            });

        }

        try {
            runnable.run();
        } finally {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    currentThread.setName(oldName);
                    return null;
                }
            });
        }
    }

}
