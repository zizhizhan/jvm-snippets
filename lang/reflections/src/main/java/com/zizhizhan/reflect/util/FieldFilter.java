package com.zizhizhan.reflect.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class FieldFilter implements MemberFilter {	

	protected final Map<String, Object> map = new LinkedHashMap<String, Object>();
	protected final Object target;
	protected final boolean includedStatic;

	public FieldFilter(Object target, boolean includedStatic) {	
		this.target = target;
		this.includedStatic = includedStatic;
	}

	public boolean accept(Member m) {
		boolean isStatic = Modifier.isStatic(m.getModifiers());
		return ((m instanceof Field) 
			&& (!isStatic && target != null  || isStatic && includedStatic)
			&& !m.getName().startsWith("this$"));			
	}

	public void handle(Member m) {
		hanleInternal((Field) m);
	}
	
	protected abstract void hanleInternal(Field f);

	public Map<String, Object> getMap() {		
		return map;
	}	

}
