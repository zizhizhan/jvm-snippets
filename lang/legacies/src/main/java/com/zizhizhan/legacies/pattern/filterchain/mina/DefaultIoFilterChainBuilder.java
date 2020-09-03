package com.zizhizhan.legacies.pattern.filterchain.mina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizhizhan.legacies.pattern.filterchain.mina.IoFilter.NextFilter;
import com.zizhizhan.legacies.pattern.filterchain.mina.IoFilterChain.Entry;

public class DefaultIoFilterChainBuilder implements IoFilterChainBuilder {

	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultIoFilterChainBuilder.class);
	private final List<Entry> entries;

	public DefaultIoFilterChainBuilder() {
		entries = new CopyOnWriteArrayList<Entry>();
	}

	public DefaultIoFilterChainBuilder(DefaultIoFilterChainBuilder filterChain) {
		if (filterChain == null) {
			throw new IllegalArgumentException("filterChain");
		}
		entries = new CopyOnWriteArrayList<Entry>(filterChain.entries);
	}

	public Entry getEntry(String name) {
		for (Entry e : entries) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	public Entry getEntry(IoFilter filter) {
		for (Entry e : entries) {
			if (e.getFilter() == filter) {
				return e;
			}
		}
		return null;
	}

	public Entry getEntry(Class<? extends IoFilter> filterType) {
		for (Entry e : entries) {
			if (filterType.isAssignableFrom(e.getFilter().getClass())) {
				return e;
			}
		}
		return null;
	}

	public IoFilter get(String name) {
		Entry e = getEntry(name);
		if (e == null) {
			return null;
		}
		return e.getFilter();
	}

	public IoFilter get(Class<? extends IoFilter> filterType) {
		Entry e = getEntry(filterType);
		if (e == null) {
			return null;
		}
		return e.getFilter();
	}

	public List<Entry> getAll() {
		return new ArrayList<Entry>(entries);
	}

	public List<Entry> getAllReversed() {
		List<Entry> result = getAll();
		Collections.reverse(result);
		return result;
	}

	public boolean contains(String name) {
		return getEntry(name) != null;
	}

	public boolean contains(IoFilter filter) {
		return getEntry(filter) != null;
	}

	public boolean contains(Class<? extends IoFilter> filterType) {
		return getEntry(filterType) != null;
	}

	public synchronized void addFirst(String name, IoFilter filter) {
		register(0, new EntryImpl(name, filter));
	}

	public synchronized void addLast(String name, IoFilter filter) {
		register(entries.size(), new EntryImpl(name, filter));
	}

	public synchronized void addBefore(String baseName, String name, IoFilter filter) {
		checkBaseName(baseName);
		for (ListIterator<Entry> i = entries.listIterator(); i.hasNext();) {
			Entry base = i.next();
			if (base.getName().equals(baseName)) {
				register(i.previousIndex(), new EntryImpl(name, filter));
				break;
			}
		}
	}

	public synchronized void addAfter(String baseName, String name, IoFilter filter) {
		checkBaseName(baseName);
		for (ListIterator<Entry> i = entries.listIterator(); i.hasNext();) {
			Entry base = i.next();
			if (base.getName().equals(baseName)) {
				register(i.nextIndex(), new EntryImpl(name, filter));
				break;
			}
		}
	}

	
	public synchronized IoFilter remove(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name");
		}
		for (ListIterator<Entry> i = entries.listIterator(); i.hasNext();) {
			Entry e = i.next();
			if (e.getName().equals(name)) {
				entries.remove(i.previousIndex());
				return e.getFilter();
			}
		}
		throw new IllegalArgumentException("Unknown filter name: " + name);
	}

	public synchronized IoFilter remove(IoFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("filter");
		}
		for (ListIterator<Entry> i = entries.listIterator(); i.hasNext();) {
			Entry e = i.next();
			if (e.getFilter() == filter) {
				entries.remove(i.previousIndex());
				return e.getFilter();
			}
		}
		throw new IllegalArgumentException("Filter not found: " + filter.getClass().getName());
	}

	public synchronized IoFilter remove(Class<? extends IoFilter> filterType) {
		if (filterType == null) {
			throw new IllegalArgumentException("filterType");
		}
		for (ListIterator<Entry> i = entries.listIterator(); i.hasNext();) {
			Entry e = i.next();
			if (filterType.isAssignableFrom(e.getFilter().getClass())) {
				entries.remove(i.previousIndex());
				return e.getFilter();
			}
		}
		throw new IllegalArgumentException("Filter not found: " + filterType.getName());
	}

	public synchronized IoFilter replace(String name, IoFilter newFilter) {
		checkBaseName(name);
		EntryImpl e = (EntryImpl) getEntry(name);
		IoFilter oldFilter = e.getFilter();
		e.setFilter(newFilter);
		return oldFilter;
	}

	public synchronized void replace(IoFilter oldFilter, IoFilter newFilter) {
		for (Entry e : entries) {
			if (e.getFilter() == oldFilter) {
				((EntryImpl) e).setFilter(newFilter);
				return;
			}
		}
		throw new IllegalArgumentException("Filter not found: " + oldFilter.getClass().getName());
	}

	public synchronized void replace(Class<? extends IoFilter> oldFilterType, IoFilter newFilter) {
		for (Entry e : entries) {
			if (oldFilterType.isAssignableFrom(e.getFilter().getClass())) {
				((EntryImpl) e).setFilter(newFilter);
				return;
			}
		}
		throw new IllegalArgumentException("Filter not found: " + oldFilterType.getName());
	}

	public synchronized void clear() {
		entries.clear();
	}

	public void setFilters(Map<String, ? extends IoFilter> filters) {
		if (filters == null) {
			throw new IllegalArgumentException("filters");
		}
		if (!isOrderedMap(filters)) {
			throw new IllegalArgumentException("filters is not an ordered map. Please try "
					+ LinkedHashMap.class.getName() + ".");
		}
		filters = new LinkedHashMap<String, IoFilter>(filters);
		for (Map.Entry<String, ? extends IoFilter> e : filters.entrySet()) {
			if (e.getKey() == null) {
				throw new IllegalArgumentException("filters contains a null key.");
			}
			if (e.getValue() == null) {
				throw new IllegalArgumentException("filters contains a null value.");
			}
		}
		synchronized (this) {
			clear();
			for (Map.Entry<String, ? extends IoFilter> e : filters.entrySet()) {
				addLast(e.getKey(), e.getValue());
			}
		}
	}	
	
	@SuppressWarnings("unchecked")
	private boolean isOrderedMap(Map<String, ? extends IoFilter> map) {
		Class<?> mapType = map.getClass();
		if (LinkedHashMap.class.isAssignableFrom(mapType)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(mapType.getSimpleName() + " is an ordered map.");
			}
			return true;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(mapType.getName() + " is not a " + LinkedHashMap.class.getSimpleName());
		}

		// Detect Jakarta Commons Collections OrderedMap implementations.
		Class<?> type = mapType;
		while (type != null) {
			for (Class<?> i : type.getInterfaces()) {
				if (i.getName().endsWith("OrderedMap")) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(mapType.getSimpleName() + " is an ordered map (guessed from that it "
								+ " implements OrderedMap interface.)");
					}
					return true;
				}
			}
			type = type.getSuperclass();
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(mapType.getName() + " doesn't implement OrderedMap interface.");
		}

		// Last resort: try to create a new instance and test if it maintains
		// the insertion order.
		LOGGER.debug("Last resort; trying to create a new map instance with a "
				+ "default constructor and test if insertion order is " + "maintained.");

		Map<String, IoFilter> newMap;
		try {			
			newMap = (Map<String, IoFilter>) mapType.newInstance();
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Failed to create a new map instance of '" + mapType.getName() + "'.", e);
			}
			return false;
		}

		Random rand = new Random();
		List<String> expectedNames = new ArrayList<String>();
		IoFilter dummyFilter = new IoFilterAdapter();
		for (int i = 0; i < 65536; i++) {
			String filterName;
			do {
				filterName = String.valueOf(rand.nextInt());
			} while (newMap.containsKey(filterName));

			newMap.put(filterName, dummyFilter);
			expectedNames.add(filterName);

			Iterator<String> it = expectedNames.iterator();
			for (Object key : newMap.keySet()) {
				if (!it.next().equals(key)) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("The specified map didn't pass the insertion " + "order test after " + (i + 1)
								+ " tries.");
					}
					return false;
				}
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("The specified map passed the insertion order test.");
		}
		return true;
	}

	public void buildFilterChain(IoFilterChain chain) throws Exception {
		for (Entry e : entries) {
			chain.addLast(e.getName(), e.getFilter());
		}
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{ ");

		boolean empty = true;

		for (Entry e : entries) {
			if (!empty) {
				buf.append(", ");
			} else {
				empty = false;
			}

			buf.append('(');
			buf.append(e.getName());
			buf.append(':');
			buf.append(e.getFilter());
			buf.append(')');
		}

		if (empty) {
			buf.append("empty");
		}

		buf.append(" }");

		return buf.toString();
	}

	private void checkBaseName(String baseName) {
		if (baseName == null) {
			throw new IllegalArgumentException("baseName");
		}

		if (!contains(baseName)) {
			throw new IllegalArgumentException("Unknown filter name: " + baseName);
		}
	}

	private void register(int index, Entry e) {
		if (contains(e.getName())) {
			throw new IllegalArgumentException("Other filter is using the same name: " + e.getName());
		}
		entries.add(index, e);
	}

	private class EntryImpl implements Entry {
		private final String name;
		private volatile IoFilter filter;

		private EntryImpl(String name, IoFilter filter) {
			if (name == null) {
				throw new IllegalArgumentException("name");
			}
			if (filter == null) {
				throw new IllegalArgumentException("filter");
			}

			this.name = name;
			this.filter = filter;
		}

		public String getName() {
			return name;
		}

		public IoFilter getFilter() {
			return filter;
		}

		private void setFilter(IoFilter filter) {
			this.filter = filter;
		}

		public NextFilter getNextFilter() {
			throw new IllegalStateException();
		}

		@Override
		public String toString() {
			return "(" + getName() + ':' + filter + ')';
		}

		public void addAfter(String name, IoFilter filter) {
			DefaultIoFilterChainBuilder.this.addAfter(getName(), name, filter);
		}

		public void addBefore(String name, IoFilter filter) {
			DefaultIoFilterChainBuilder.this.addBefore(getName(), name, filter);
		}

		public void remove() {
			DefaultIoFilterChainBuilder.this.remove(getName());
		}

		public void replace(IoFilter newFilter) {
			DefaultIoFilterChainBuilder.this.replace(getName(), newFilter);
		}
	}
}