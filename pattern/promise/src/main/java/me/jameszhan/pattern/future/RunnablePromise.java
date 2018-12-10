package me.jameszhan.pattern.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class RunnablePromise<V> extends Promise<V> implements Runnable {

    /** The underlying callable; nulled out after running */
    private Callable<V> callable;
    /** The thread running the callable; CASed during run() */
    private volatile Thread runner;

    public RunnablePromise(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        if (state != NEW || !UNSAFE.compareAndSwapObject(this, runnerOffset,null, Thread.currentThread())) {
            return;
        }
        try {
            Callable<V> c = callable;
            if (c != null && state == NEW) {
                V result;
                boolean ran;
                try {
                    result = c.call();
                    ran = true;
                } catch (Throwable ex) {
                    result = null;
                    ran = false;
                    setException(ex);
                }
                if (ran) {
                    set(result);
                }
            }
        } finally {
            // runner must be non-null until state is settled to prevent concurrent calls to run()
            runner = null;
            // state must be re-read after nulling runner to prevent leaked interrupts
            int s = state;
            if (s >= INTERRUPTING) {
                handlePossibleCancellationInterrupt(s);
            }
        }
    }

    /**
     * Sets the result of this future to the given value unless
     * this future has already been set or has been cancelled.
     *
     * <p>This method is invoked internally by the {@link #run} method
     * upon successful completion of the computation.
     *
     * @param v the value
     */
    protected void set(V v) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            outcome = v;
            UNSAFE.putOrderedInt(this, stateOffset, NORMAL); // final state
            finishCompletion();
        }
    }

    /**
     * Causes this future to report an {@link ExecutionException}
     * with the given throwable as its cause, unless this future has
     * already been set or has been cancelled.
     *
     * <p>This method is invoked internally by the {@link #run} method
     * upon failure of the computation.
     *
     * @param t the cause of failure
     */
    protected void setException(Throwable t) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
            outcome = t;
            UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL); // final state
            finishCompletion();
        }
    }

    /**
     * Ensures that any interrupt from a possible cancel(true) is only
     * delivered to a task while in run or runAndReset.
     */
    private void handlePossibleCancellationInterrupt(int s) {
        // It is possible for our interrupter to stall before getting a
        // chance to interrupt us.  Let's spin-wait patiently.
        if (s == INTERRUPTING) {
            while (state == INTERRUPTING) {
                Thread.yield(); // wait out pending interrupt
            }
        }
        // assert state == INTERRUPTED;

        // We want to clear any interrupt we may have received from
        // cancel(true).  However, it is permissible to use interrupts
        // as an independent mechanism for a task to communicate with
        // its caller, and there is no way to clear only the
        // cancellation interrupt.
        //
        // Thread.interrupted();
    }

    private static final long runnerOffset;
    static {
        try {
            Class<?> k = RunnablePromise.class;
            runnerOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("runner"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
