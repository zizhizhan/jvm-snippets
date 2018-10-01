package com.google.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class NioProcessor
{

	private static final long SELECT_TIMEOUT = 100000L;
	private final Selector selector;

	private final Queue<NioSession> newSessions = new ConcurrentLinkedQueue<NioSession>();
	private final Queue<NioSession> removingSessions = new ConcurrentLinkedQueue<NioSession>();
	private final Queue<NioSession> flushingSessions = new ConcurrentLinkedQueue<NioSession>();
	private final Queue<NioSession> trafficControllingSessions = new ConcurrentLinkedQueue<NioSession>();

	private long lastIdleCheckTime;

	private Processor processor;

	private Executor executor = new ThreadExecutor();

	public NioProcessor()
	{
		try
		{
			selector = Selector.open();
		} catch (IOException ex)
		{
			throw new RuntimeException("Can't open selector.", ex);
		}
	}

	public void process(NioSession session)
	{
		newSessions.add(session);
		startupProcessor();
	}

	private int handleNewSessions()
	{
		int addedSessions = 0;
		for (;;)
		{
			NioSession session = newSessions.poll();

			if (session == null)
			{
				break;
			}

			System.out.println(newSessions.size());
			if (processNow(session))
			{
				addedSessions++;
			}
		}
		return addedSessions;
	}

	private boolean processNow(NioSession session)
	{
		try
		{
			SelectableChannel ch = (SelectableChannel) session.getChannel();
			ch.configureBlocking(false);
			session.setSelectionKey(ch.register(selector, SelectionKey.OP_READ, session));
			System.out.println("Session create success!");
			return true;
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	private void startupProcessor()
	{
		if (processor == null)
		{
			processor = new Processor();
			executor.execute(processor);
		}
		selector.wakeup();
	}

	private void handle() throws Exception
	{
		for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();)
		{
			NioSession session = (NioSession) i.next().attachment();
			handle(session);
			i.remove();
		}
	}

	private void handle(NioSession session)
	{
		if (isReadable(session) && !session.isReadSuspended())
		{
			read(session);
		}
		/*
		 * if (isWritable(session) && !session.isWriteSuspended()) {
		 * scheduleFlush(session); }
		 */
	}

	private void scheduleFlush(NioSession session)
	{

	}

	private void read(NioSession session)
	{
		ByteBuffer buf = ByteBuffer.allocate(1024);

		try
		{
			int readBytes = 0;
			int ret;
			try
			{
				ret = session.getChannel().read(buf);
				if (ret > 0)
				{
					readBytes = ret;
				}
			} finally
			{
				buf.flip();
			}

			if (readBytes > 0)
			{
				System.out.println(Charset.defaultCharset().decode(buf));
				session.getChannel().write(buf);
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	protected boolean isReadable(NioSession session)
	{
		SelectionKey key = session.getSelectionKey();
		return key.isValid() && key.isReadable();
	}

	protected boolean isWritable(NioSession session)
	{
		SelectionKey key = session.getSelectionKey();
		return key.isValid() && key.isWritable();
	}

	private void flush(long currentTime)
	{
		final NioSession firstSession = flushingSessions.peek();
		if (firstSession == null)
		{
			return;
		}

		NioSession session = flushingSessions.poll();
		for (;;)
		{
			SessionState state = state(session);

			switch (state)
			{
			case OPEN:
				try
				{
					// boolean flushedAll = flushNow(session, currentTime);
					// if (flushedAll &&
					// !session.getWriteRequestQueue().isEmpty(session) &&
					// !session.isScheduledForFlush()) {
					// scheduleFlush(session);
					// }
				} catch (Exception e)
				{
					// scheduleRemove(session);
					// IoFilterChain filterChain = session.getFilterChain();
					// filterChain.fireExceptionCaught(e);
				}
				break;
			case CLOSED:
				// Skip if the channel is already closed.
				break;
			case PREPARING:
				// Retry later if session is not yet fully initialized.
				// (In case that Session.write() is called before addSession()
				// is processed)
				scheduleFlush(session);
				return;
			default:
				throw new IllegalStateException(String.valueOf(state));
			}

			session = flushingSessions.peek();
			if (session == null || session == firstSession)
			{
				break;
			}
			session = flushingSessions.poll();
		}
	}

	protected SessionState state(NioSession session)
	{
		SelectionKey key = session.getSelectionKey();
		if (key == null)
		{
			return SessionState.PREPARING;
		}
		return key.isValid() ? SessionState.OPEN : SessionState.CLOSED;
	}

	private class Processor implements Runnable
	{
		@Override
		public void run()
		{
			int nSessions = 0;
			lastIdleCheckTime = System.currentTimeMillis();

			for (;;)
			{
				try
				{
					int selected = selector.select(SELECT_TIMEOUT);
					nSessions += handleNewSessions();

					if (selected > 0)
					{
						handle();
					}

					// long currentTime = System.currentTimeMillis();
					// flush(currentTime);
					// nSessions -= remove();
					// notifyIdleSessions(currentTime);

					if (nSessions == 0)
					{
						if (newSessions.isEmpty() && selector.keys().isEmpty())
						{
							processor = null;
							break;
						}
					}
				} catch (Exception ex)
				{
					ex.printStackTrace();
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

		}
	}

	protected static enum SessionState
	{
		OPEN, CLOSED, PREPARING,
	}
}
