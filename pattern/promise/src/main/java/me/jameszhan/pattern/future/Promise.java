package me.jameszhan.pattern.future;

import lombok.extern.slf4j.Slf4j;
import me.jameszhan.pattern.util.Unsafes;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * @see FutureTask
 */
@Slf4j
public class Promise<V> implements Future<V> {

    /**
     * Possible state transitions:
     * NEW -> COMPLETING -> NORMAL
     * NEW -> COMPLETING -> EXCEPTIONAL
     * NEW -> CANCELLED
     * NEW -> INTERRUPTING -> INTERRUPTED
     */
    volatile int state;
    static final int NEW          = 0;
    static final int COMPLETING   = 1;
    static final int NORMAL       = 2;
    static final int EXCEPTIONAL  = 3;
    private static final int CANCELLED    = 4;
    static final int INTERRUPTING = 5;
    private static final int INTERRUPTED  = 6;

    /** The result to return or exception to throw from get() */
    Object outcome; // non-volatile, protected by state reads/writes
    /** The thread running the callable; CASed during run() */
    private volatile Thread runner;
    /** Treiber stack of waiting threads */
    private volatile WaitNode waiters;

    public Promise() {
        this.state = NEW;
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!(state == NEW && UNSAFE.compareAndSwapInt(this, stateOffset, NEW,
                mayInterruptIfRunning ? INTERRUPTING : CANCELLED))) {
            return false;
        }
        try {    // in case call to interrupt throws exception
            if (mayInterruptIfRunning) {
                try {
                    Thread t = runner;
                    if (t != null) {
                        t.interrupt();
                    }
                } finally { // final state
                    UNSAFE.putOrderedInt(this, stateOffset, INTERRUPTED);
                }
            }
        } finally {
            finishCompletion();
        }
        return true;
    }

    @Override
    public boolean isCancelled() {
        return state >= CANCELLED;
    }

    @Override
    public boolean isDone() {
        return state != NEW;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        int s = state;
        if (s <= COMPLETING) {
            s = awaitDone(false, 0L);
        }
        return report(s);
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null) {
            throw new NullPointerException();
        }
        int s = state;
        if (s <= COMPLETING && (s = awaitDone(true, unit.toNanos(timeout))) <= COMPLETING) {
            throw new TimeoutException();
        }
        return report(s);
    }

    /**
     * Returns result or throws exception for completed task.
     *
     * @param s completed state value
     */
    @SuppressWarnings("unchecked")
    private V report(int s) throws ExecutionException {
        Object x = outcome;
        if (s == NORMAL) {
            return (V) x;
        }
        if (s >= CANCELLED) {
            throw new CancellationException();
        } else {
            throw new ExecutionException((Throwable) x);
        }
    }

    /**
     * Removes and signals all waiting threads, invokes done(), and
     * nulls out callable.
     */
    void finishCompletion() {
        // assert state > COMPLETING;
        for (WaitNode q; (q = waiters) != null;) {
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
                for (;;) {
                    Thread t = q.thread;
                    if (t != null) {
                        q.thread = null;
                        LockSupport.unpark(t);
                    }
                    WaitNode next = q.next;
                    if (next == null) {
                        break;
                    }
                    q.next = null; // unlink to help gc
                    q = next;
                }
                break;
            }
        }
    }

    /**
     * Awaits completion or aborts on interrupt or timeout.
     *
     * @param timed true if use timed waits
     * @param nanos time to wait, if timed
     * @return state upon completion
     */
    private int awaitDone(boolean timed, long nanos) throws InterruptedException {
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        WaitNode q = null;
        boolean queued = false;
        for (;;) {
            if (Thread.interrupted()) {
                removeWaiter(q);
                throw new InterruptedException();
            }

            int s = state;
            if (s > COMPLETING) {
                if (q != null) {
                    q.thread = null;
                }
                return s;
            } else if (s == COMPLETING) { // cannot time out yet
                Thread.yield();
            } else if (q == null) {
                q = new WaitNode();
            } else if (!queued) {
//                q.next = waiters;
//                this.waiters = q;
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset, q.next = waiters, q);
                log.debug("queued {} {}.", q, queued ? "success" : "failure");
            } else if (timed) {
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L) {
                    removeWaiter(q);
                    return state;
                }
                LockSupport.parkNanos(this, nanos);
            } else {
                LockSupport.park(this);
            }
        }
    }

    /**
     * Tries to unlink a timed-out or interrupted wait node to avoid
     * accumulating garbage.  Internal nodes are simply unspliced
     * without CAS since it is harmless if they are traversed anyway
     * by releasers.  To avoid effects of unsplicing from already
     * removed nodes, the list is retraversed in case of an apparent
     * race.  This is slow when there are a lot of nodes, but we don't
     * expect lists to be long enough to outweigh higher-overhead
     * schemes.
     */
    private void removeWaiter(WaitNode node) {
        if (node != null) {
            node.thread = null;

            retry:
            for (;;) { // restart on removeWaiter race
                for (WaitNode pred = null, q = waiters, s; q != null; q = s) {
                    s = q.next;
                    if (q.thread != null) {
                        pred = q;
                    } else if (pred != null) {
                        pred.next = s;
                        if (pred.thread == null) { // check for race
                            continue retry;
                        }
                    } else if (!UNSAFE.compareAndSwapObject(this, waitersOffset, q, s)) {
                        continue retry;
                    }
                }
                break;
            }
        }
    }

    // Unsafe mechanics
    static final sun.misc.Unsafe UNSAFE;
    static final long stateOffset;
    private static final long waitersOffset;
    static {
        try {
            UNSAFE = Unsafes.getUnsafe();
            Class<?> k = Promise.class;
            stateOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("state"));
            waitersOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * Simple linked list nodes to record waiting threads in a Treiber
     * stack.  See other classes such as Phaser and SynchronousQueue
     * for more detailed explanation.
     */
    static final class WaitNode {
        volatile Thread thread;
        volatile WaitNode next;
        WaitNode() { thread = Thread.currentThread(); }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("WaitNode");
            sb.append('@').append(this.hashCode()).append('[');
            WaitNode current = next;
            sb.append(thread.getName());
            while (current != null) {
                sb.append(", ").append(current.thread.getName());
                current = current.next;
            }
            sb.append(']');
            return sb.toString();

        }
    }

}
