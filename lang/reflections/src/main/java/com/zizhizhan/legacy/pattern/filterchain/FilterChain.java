package com.zizhizhan.legacy.pattern.filterchain;

import java.util.List;

import com.zizhizhan.legacy.pattern.filterchain.Filter.NextFilter;

public interface FilterChain {

	public abstract List<Entry> getAll();

	public abstract List<Entry> getAllReversed();

	public abstract FilterChain addFirst(String name, Filter filter);

	public abstract FilterChain addLast(String name, Filter filter);

	public abstract FilterChain addBefore(String baseName, String name, Filter filter);

	public abstract FilterChain addAfter(String baseName, String name, Filter filter);

	public abstract Filter replace(String name, Filter newFilter);

	public abstract Filter replace(Class<? extends Filter> oldFilterType, Filter newFilter);

	public abstract FilterChain replace(Filter oldFilter, Filter newFilter);

	public abstract Filter remove(String name);

	public abstract Filter remove(Class<? extends Filter> filterType);

	public abstract FilterChain remove(Filter filter);

	public abstract void clear() throws Exception;

	public abstract Filter get(String name);

	public abstract Filter get(Class<? extends Filter> filterType);

	public abstract Entry getEntry(String name);

	public abstract Entry getEntry(Filter filter);

	public abstract Entry getEntry(Class<? extends Filter> filterType);

	public abstract boolean contains(String name);

	public abstract boolean contains(Filter filter);

	public abstract boolean contains(Class<? extends Filter> filterType);

	public abstract void fireProcess();

	public abstract void fireClose();

	public interface Entry {

		String getName();

		Filter getFilter();

		void addBefore(String name, Filter filter);

		void addAfter(String name, Filter filter);

		void replace(Filter newFilter);

		void remove();

		NextFilter getNextFilter();
	}

}
