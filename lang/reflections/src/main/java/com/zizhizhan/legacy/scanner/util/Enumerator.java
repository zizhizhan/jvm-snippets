package com.zizhizhan.legacy.scanner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Enumerator<E> implements Enumeration<E> {
	
	private Iterator<E> iterator = null;
	
    public Enumerator(Collection<E> collection) {
        this(collection.iterator());
    }

    public Enumerator(Collection<E> collection, boolean clone) {
        this(collection.iterator(), clone);
    }

    public Enumerator(Iterator<E> iterator) {
        super();
        this.iterator = iterator;
    }

    public Enumerator(Iterator<E> iterator, boolean clone) {
        super();
        if (!clone) {
            this.iterator = iterator;
        } else {
            List<E> list = new ArrayList<E>();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            this.iterator = list.iterator();   
        }

    }

    public Enumerator(Map<?, E> map) {
        this(map.values().iterator());
    }

    public Enumerator(Map<?, E> map, boolean clone) {
        this(map.values().iterator(), clone);
    }

    public boolean hasMoreElements() {
        return (iterator.hasNext());
    }

    public E nextElement() throws NoSuchElementException {
        return (iterator.next());
    }

}
