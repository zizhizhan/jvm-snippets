package com.zizhizhan.legacies.pattern.filterchain.v1;

import java.util.List;

import com.zizhizhan.legacies.pattern.filterchain.v1.Filter.NextFilter;

public interface FilterChain {

	List<Entry> getAll();

	List<Entry> getAllReversed();

	FilterChain addFirst(String name, Filter filter);

	FilterChain addLast(String name, Filter filter);

	FilterChain addBefore(String baseName, String name, Filter filter);

	FilterChain addAfter(String baseName, String name, Filter filter);

	Filter replace(String name, Filter newFilter);

	Filter replace(Class<? extends Filter> oldFilterType, Filter newFilter);

	FilterChain replace(Filter oldFilter, Filter newFilter);

	Filter remove(String name);

	Filter remove(Class<? extends Filter> filterType);

	FilterChain remove(Filter filter);

	void clear() throws Exception;

	Filter get(String name);

	Filter get(Class<? extends Filter> filterType);

	Entry getEntry(String name);

	Entry getEntry(Filter filter);

	Entry getEntry(Class<? extends Filter> filterType);

	boolean contains(String name);

	boolean contains(Filter filter);

	boolean contains(Class<? extends Filter> filterType);

	void fireProcess();

	void fireClose();

	interface Entry {

		String getName();

		Filter getFilter();

		void addBefore(String name, Filter filter);

		void addAfter(String name, Filter filter);

		void replace(Filter newFilter);

		void remove();

		NextFilter getNextFilter();
	}

}
