/**
 * Copyright 2006 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.zizhizhan.legacies.juc;


/**
 * Monitor class for asynchronous examples. Producer signals end of message
 * stream; listener calls allDone() to notify consumer that the signal has
 * arrived, while consumer calls waitTillDone() to wait for this notification.
 * Class supplied by Sun Microsystems. <p/> <b> Modifications made to ensure
 * that waitTillDone() always blocks when called and is only released by a
 * subsequent call to allDone(). Therefore, if multiple calls are made to
 * allDone(), waitTillDone() will still block when it's called. Also added a
 * waitTillDone(long) that takes a timeout value. It will either wait until done
 * is true or a timeout occurs. </b>
 */
public final class DoneLatch {
    public boolean m_done = false;

    /**
     * Blocks the current thread until done is true and the thread has been
     * notified by allDone().
     */
    public void waitTillDone() {
        synchronized (this) {
            while (!m_done) {
                try {
                    this.wait();
                } catch (InterruptedException ie) {
                }
            }
            m_done = false;
        }
    }

    /**
     * Blocks the current thread until done is true and the thread has been
     * notified or a timeout occurs. Timeout is in milliseconds.
     *
     * @param timeout timeout in milliseconds
     */
    public void waitTillDone(long timeout) {
        boolean isTimedOut = false;
        synchronized (this) {
            while (!m_done && !isTimedOut) {
                try {
                    this.wait(timeout);
                    isTimedOut = true;
                } catch (InterruptedException ie) {
                }
            }
            m_done = false;
        }
    }

    /**
     * Sets done to true and notifies the waiting thread to unblock.
     */
    public void allDone() {
        synchronized (this) {
            m_done = true;
            this.notify();
        }
    }

    /**
     * Sets done to the given value.
     *
     * @param done
     */
    public void setDone(boolean done) {
        synchronized (this) {
            this.m_done = done;
        }
    }

    /**
     * Gets done.
     *
     * @return true if done, false otherwise
     */
    public boolean getDone() {
        return this.m_done;
    }

}