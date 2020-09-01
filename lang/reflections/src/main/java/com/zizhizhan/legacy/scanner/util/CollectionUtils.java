package com.zizhizhan.legacy.scanner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class CollectionUtils {
	
	private CollectionUtils(){}
	
	public static <T> Collection<T> union(Collection<T> c1, Collection<T> c2){
		Set<T> set = new HashSet<T>();
		set.addAll(c1);
		set.addAll(c2);
		return set;
	}
	
	public static <T> Collection<T> intersect(Collection<T> c1, Collection<T> c2){
		if(c1 == null || c2 == null){
			return null;
		}
		Collection<T> ret = new ArrayList<T>();
		for(T obj : c1){
			if(c2.contains(obj)){
				ret.add(obj);
			}
		}
		return ret;
	}
	
	public static <T> Collection<T> diff(Collection<T> c1, Collection<T> c2){
		if (c1 == null || c1.size() == 0 || c2 == null || c2.size() == 0) {
            return c1;
        }
        Collection<T> difference = new ArrayList<T>();
        for (T item : c1) {
            if (!c2.contains(item)) {
                difference.add(item);
            }
        }
        return difference;
	}

}
