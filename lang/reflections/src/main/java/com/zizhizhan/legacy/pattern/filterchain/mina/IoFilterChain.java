package com.zizhizhan.legacy.pattern.filterchain.mina;

import java.util.List;
import com.zizhizhan.legacy.pattern.filterchain.mina.IoFilter.NextFilter;

public interface IoFilterChain {

	IoSession getSession();

	Entry getEntry(String name);

	Entry getEntry(IoFilter filter);

	Entry getEntry(Class<? extends IoFilter> filterType);

	IoFilter get(String name);

	IoFilter get(Class<? extends IoFilter> filterType);

	NextFilter getNextFilter(String name);

	NextFilter getNextFilter(IoFilter filter);

	NextFilter getNextFilter(Class<? extends IoFilter> filterType);

	List<Entry> getAll();

	List<Entry> getAllReversed();

	boolean contains(String name);

	boolean contains(IoFilter filter);

	boolean contains(Class<? extends IoFilter> filterType);

	void addFirst(String name, IoFilter filter);

	void addLast(String name, IoFilter filter);

	void addBefore(String baseName, String name, IoFilter filter);

	void addAfter(String baseName, String name, IoFilter filter);

	IoFilter replace(String name, IoFilter newFilter);

	void replace(IoFilter oldFilter, IoFilter newFilter);

	IoFilter replace(Class<? extends IoFilter> oldFilterType, IoFilter newFilter);

	IoFilter remove(String name);

	void remove(IoFilter filter);

	IoFilter remove(Class<? extends IoFilter> filterType);

	void clear() throws Exception;

	public void fireSessionCreated();

	public void fireSessionOpened();

	public void fireSessionClosed();

	public void fireSessionIdle(IdleStatus status);

	public void fireMessageReceived(Object message);

	public void fireMessageSent(WriteRequest request);

	public void fireExceptionCaught(Throwable cause);

	public void fireFilterWrite(WriteRequest writeRequest);

	public void fireFilterClose();

	public interface Entry {

		String getName();

		IoFilter getFilter();

		NextFilter getNextFilter();

		void addBefore(String name, IoFilter filter);

		void addAfter(String name, IoFilter filter);

		void replace(IoFilter newFilter);

		void remove();
	}
}
