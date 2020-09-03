package com.zizhizhan.legacies.juc;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CallFuture implements Future<String> {

	private final Sync sync;

	public CallFuture() {
		this.sync = new Sync();
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return sync.innerCancel(mayInterruptIfRunning);
	}

	@Override
	public String get() throws InterruptedException, ExecutionException {
		return sync.innerGet();
	}

	@Override
	public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return sync.innerGet(unit.toNanos(timeout));
	}

	@Override
	public boolean isCancelled() {
		return sync.innerIsCancelled();
	}

	@Override
	public boolean isDone() {
		return sync.innerIsDone();
	}

	protected void done() {
	}

	protected void set(String v) {
		sync.innerSet(v);
	}

	protected void setException(Throwable t) {
		sync.innerSetException(t);
	}

	final class Sync extends AbstractQueuedSynchronizer {

		private static final long serialVersionUID = -7828117401763700385L;

		private static final int RUNNING = 1;

		private static final int RAN = 2;

		private static final int CANCELLED = 4;

		private String result;

		private Throwable exception;

		private volatile Thread runner;

		private boolean ranOrCancelled(int state) {
			return (state & (RAN | CANCELLED)) != 0;
		}

		protected int tryAcquireShared(int ignore) {
			return innerIsDone() ? 1 : -1;
		}

		protected boolean tryReleaseShared(int ignore) {
			runner = null;
			return true;
		}

		boolean innerIsCancelled() {
			return getState() == CANCELLED;
		}

		boolean innerIsDone() {
			return ranOrCancelled(getState()) && runner == null;
		}

		String innerGet() throws InterruptedException, ExecutionException {
			acquireSharedInterruptibly(0);
			if (getState() == CANCELLED)
				throw new CancellationException();
			if (exception != null)
				throw new ExecutionException(exception);
			return result;
		}

		String innerGet(long nanosTimeout) throws InterruptedException, ExecutionException, TimeoutException {
			if (!tryAcquireSharedNanos(0, nanosTimeout))
				throw new TimeoutException();
			if (getState() == CANCELLED)
				throw new CancellationException();
			if (exception != null)
				throw new ExecutionException(exception);
			return result;
		}

		void innerSet(String v) {
			for (;;) {
				int s = getState();
				if (s == RAN)
					return;
				if (s == CANCELLED) {
					releaseShared(0);
					return;
				}
				if (compareAndSetState(s, RAN)) {
					result = v;
					releaseShared(0);
					done();
					return;
				}
			}
		}

		void innerSetException(Throwable t) {
			for (;;) {
				int s = getState();
				if (s == RAN)
					return;
				if (s == CANCELLED) {
					releaseShared(0);
					return;
				}
				if (compareAndSetState(s, RAN)) {
					exception = t;
					result = null;
					releaseShared(0);
					done();
					return;
				}
			}
		}

		boolean innerCancel(boolean mayInterruptIfRunning) {
			for (;;) {
				int s = getState();
				if (ranOrCancelled(s)) {
					return false;
				}
				if (compareAndSetState(s, CANCELLED)) {
					break;
				}
			}
			if (mayInterruptIfRunning) {
				Thread r = runner;
				if (r != null) {
					r.interrupt();
				}
			}
			releaseShared(0);
			done();
			return true;
		}

		void innerRun() {
			if (!compareAndSetState(0, RUNNING))
				return;
			try {
				runner = Thread.currentThread();
				if (getState() == RUNNING) {// recheck after setting thread
					// innerSet(callable.call());
				} else {
					releaseShared(0); // cancel
				}
			} catch (Throwable ex) {
				innerSetException(ex);
			}
		}

		boolean innerRunAndReset() {
			if (!compareAndSetState(0, RUNNING))
				return false;
			try {
				runner = Thread.currentThread();
				if (getState() == RUNNING)
					// callable.call(); // don't set result
					runner = null;
				return compareAndSetState(RUNNING, 0);
			} catch (Throwable ex) {
				innerSetException(ex);
				return false;
			}
		}
	}

}
