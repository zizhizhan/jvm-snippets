package com.zizhizhan.legacy.pattern.filterchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zizhizhan.legacy.pattern.filterchain.Filter.NextFilter;

public class DefaultFilterChain implements FilterChain {
	
	private final Map<String, Entry> entryMap = new ConcurrentHashMap<String, Entry>();
	private final EntryImpl head;
	private final EntryImpl tail;
	private final Exchange exchange;
	
	public DefaultFilterChain(Exchange exchange) {
		head = new EntryImpl(null, null, "head", new HeadFilter());
		tail = new EntryImpl(head, null, "tail", new TailFilter());
		head.nextEntry = tail;
		this.exchange = exchange;
	}

	@Override
	public List<Entry> getAll() {
		List<Entry> list = new ArrayList<Entry>();
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			list.add(e);
			e = e.nextEntry;
		}
		return list;
	}

	@Override
	public List<Entry> getAllReversed() {
		List<Entry> list = new ArrayList<Entry>();
		EntryImpl e = tail.prevEntry;
		while (e != head) {
			list.add(e);
			e = e.prevEntry;
		}
		return list;
	}

	@Override
	public synchronized FilterChain addFirst(String name, Filter filter) {
		checkAddable(name);
		register(head, name, filter);
		return this;
	}

	@Override
	public synchronized FilterChain addLast(String name, Filter filter) {
		checkAddable(name);
		register(tail.prevEntry, name, filter);
		return this;
	}

	@Override
	public synchronized FilterChain addBefore(String baseName, String name, Filter filter) {
		EntryImpl baseEntry = checkOldName(baseName);
		checkAddable(name);
		register(baseEntry.prevEntry, name, filter);
		return this;
	}

	@Override
	public synchronized FilterChain addAfter(String baseName, String name, Filter filter) {
		EntryImpl baseEntry = checkOldName(baseName);
		checkAddable(name);
		register(baseEntry, name, filter);
		return this;
	}

	@Override
	public synchronized Filter replace(String name, Filter newFilter) {
		EntryImpl entry = checkOldName(name);
		Filter oldFilter = entry.getFilter();
		entry.setFilter(newFilter);
		return oldFilter;
	}

	@Override
	public synchronized Filter replace(Class<? extends Filter> oldFilterType, Filter newFilter) {
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			if (oldFilterType.isAssignableFrom(e.getFilter().getClass())) {
				Filter oldFilter = e.getFilter();
				e.setFilter(newFilter);
				return oldFilter;
			}
			e = e.nextEntry;
		}
		throw new IllegalArgumentException("Filter not found: " + oldFilterType.getName());
	}

	@Override
	public synchronized FilterChain replace(Filter oldFilter, Filter newFilter) {
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			if (e.getFilter() == oldFilter) {
				e.setFilter(newFilter);
				return this;
			}
			e = e.nextEntry;
		}
		throw new IllegalArgumentException("Filter not found: " + oldFilter.getClass().getName());
	}

	@Override
	public synchronized Filter remove(String name) {
		EntryImpl entry = checkOldName(name);
		deregister(entry);
		return entry.getFilter();
	}

	@Override
	public synchronized Filter remove(Class<? extends Filter> filterType) {
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			if (filterType.isAssignableFrom(e.getFilter().getClass())) {
				Filter oldFilter = e.getFilter();
				deregister(e);
				return oldFilter;
			}
			e = e.nextEntry;
		}
		throw new IllegalArgumentException("Filter not found: " + filterType.getName());
	}

	@Override
	public synchronized FilterChain remove(Filter filter) {
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			if (e.getFilter() == filter) {
				deregister(e);
				return this;
			}
			e = e.nextEntry;
		}
		throw new IllegalArgumentException("Filter not found: " + filter.getClass().getName());
	}

	@Override
	public synchronized void clear() throws Exception {
		List<Entry> l = new ArrayList<Entry>(entryMap.values());
		for (Entry entry : l) {
			try {
				deregister((EntryImpl) entry);
			} catch (Exception e) {
				throw new IllegalStateException("clear(): " + entry.getName() + " in " + this, e);
			}
		}
	}

	@Override
	public Filter get(String name) {
		Entry e = getEntry(name);
		if (e == null) {
			return null;
		}

		return e.getFilter();
	}

	@Override
	public Filter get(Class<? extends Filter> filterType) {
		Entry e = getEntry(filterType);
		if (e == null) {
			return null;
		}
		return e.getFilter();
	}

	@Override
	public Entry getEntry(String name) {
		Entry e = entryMap.get(name);
		if (e == null) {
			return null;
		}
		return e;
	}

	@Override
	public Entry getEntry(Filter filter) {
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			if (e.getFilter() == filter) {
				return e;
			}
			e = e.nextEntry;
		}
		return null;
	}

	@Override
	public Entry getEntry(Class<? extends Filter> filterType) {
		EntryImpl e = head.nextEntry;
		while (e != tail) {
			if (filterType.isAssignableFrom(e.getFilter().getClass())) {
				return e;
			}
			e = e.nextEntry;
		}
		return null;
	}

	@Override
	public boolean contains(String name) {
		return getEntry(name) != null;
	}

	@Override
	public boolean contains(Filter filter) {
		return getEntry(filter) != null;
	}

	@Override
	public boolean contains(Class<? extends Filter> filterType) {
		return getEntry(filterType) != null;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{ ");
		boolean empty = true;
		EntryImpl e = head.nextEntry;
		while (e != tail) {
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
			e = e.nextEntry;
		}
		if (empty) {
			buf.append("empty");
		}
		buf.append(" }");
		return buf.toString();
	}

	
	private EntryImpl checkOldName(String baseName) {
		EntryImpl e = (EntryImpl) entryMap.get(baseName);
		if (e == null) {
			throw new IllegalArgumentException("Filter not found:" + baseName);
		}
		return e;
	}
	
	private void checkAddable(String name) {
		if (entryMap.containsKey(name)) {
			throw new IllegalArgumentException("Other filter is using the same name '" + name + "'");
		}
	}
	
	private void register(EntryImpl prevEntry, String name, Filter filter) {
		EntryImpl newEntry = new EntryImpl(prevEntry, prevEntry.nextEntry, name, filter);
		prevEntry.nextEntry.prevEntry = newEntry;
		prevEntry.nextEntry = newEntry;
		
		entryMap.put(name, newEntry);
	}
	
	private void deregister(EntryImpl entry) {
		EntryImpl prevEntry = entry.prevEntry;
		EntryImpl nextEntry = entry.nextEntry;
		prevEntry.nextEntry = nextEntry;
		nextEntry.prevEntry = prevEntry;

		entryMap.remove(entry.name);
	}
	
	@Override
	public void fireProcess(){
		Entry head = this.head;
		callNextProcess(head, exchange);
	}

	@Override
	public void fireClose(){
		Entry tail = this.tail;
		callPreviousClose(tail, exchange);
	}
	
	private void callNextProcess(Entry entry, Exchange exchange){
		Filter filter = entry.getFilter();
		NextFilter nextFilter = entry.getNextFilter();
		filter.process(nextFilter, exchange);
	}
	
	private void callPreviousClose(Entry entry, Exchange exchange){
		Filter filter = entry.getFilter();
		NextFilter nextFilter = entry.getNextFilter();
		filter.close(nextFilter, exchange);
	}
		
	private static class HeadFilter extends FilterAdapter {
		public void close(NextFilter nextFilter, Exchange exchange) {
			exchange.close();
		}
	}		
	
	private static class TailFilter extends FilterAdapter {
		public void process(NextFilter nextFilter, Exchange exchange) {
			exchange.process();
		}	
	}	
	
	private class EntryImpl implements Entry{
		
		private final String name;	
		private final NextFilter nextFilter;
		private EntryImpl prevEntry;		
		private EntryImpl nextEntry;		
		private Filter filter;
		
		private EntryImpl(EntryImpl prevEntry, EntryImpl nextEntry, String name, Filter filter) {
			if (filter == null) {
				throw new IllegalArgumentException("filter");
			}
			if (name == null) {
				throw new IllegalArgumentException("name");
			}
			this.name = name;
			this.filter = filter;
			this.prevEntry = prevEntry;
			this.nextEntry = nextEntry;
			this.nextFilter = new NextFilter() {				
				public void process(Exchange exchange) {
					Entry nextEntry = EntryImpl.this.nextEntry;
					callNextProcess(nextEntry, exchange);
				}
				public void close(Exchange exchange) {
					Entry nextEntry = EntryImpl.this.prevEntry;
					callPreviousClose(nextEntry, exchange);
				}				
			};		
		}
		
		public NextFilter getNextFilter() {
			return nextFilter;
		}
		
		public String getName() {
			return name;
		}

		public Filter getFilter() {
			return filter;
		}

		private void setFilter(Filter filter) {
			if (filter == null) {
				throw new IllegalArgumentException("filter");
			}
			this.filter = filter;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("('").append(getName()).append('\'');
			sb.append(", prev: '");
			if (prevEntry != null) {
				sb.append(prevEntry.name);
				sb.append(':');
				sb.append(prevEntry.getFilter().getClass().getSimpleName());
			} else {
				sb.append("null");
			}
			sb.append("', next: '");
			if (nextEntry != null) {
				sb.append(nextEntry.name);
				sb.append(':');
				sb.append(nextEntry.getFilter().getClass().getSimpleName());
			} else {
				sb.append("null");
			}
			sb.append("')");
			return sb.toString();
		}

		public void addAfter(String name, Filter filter) {
			DefaultFilterChain.this.addAfter(getName(), name, filter);
		}

		public void addBefore(String name, Filter filter) {
			DefaultFilterChain.this.addBefore(getName(), name, filter);
		}

		public void remove() {
			DefaultFilterChain.this.remove(getName());
		}

		public void replace(Filter newFilter) {
			DefaultFilterChain.this.replace(getName(), newFilter);
		}			
	}

}
