package com.zizhizhan.prototypes.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MockObject implements InvocationHandler, Mockable {
	
	private final Map<Method, Queue<Object>> map = new LinkedHashMap<Method, Queue<Object>>();
	private MockException t;
	private boolean replayed;
	private Queue<Object> currentQueue;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (isLocalMethod(method)) {			
			return method.invoke(this, args);
		} 
		if(replayed){
			return invokeInternal(method);
		}else {
			Queue<Object> queue = map.get(method);
			if (queue == null) {
				queue = new LinkedList<Object>();
				map.put(method, queue);
				if(method.getReturnType().equals(void.class)){
					queue.offer(VoidObject.INSTANCE);
				}
			}
			currentQueue = queue;
			MockControl.s_currentMockObject = this;
			return null;
		}
	}
	
	private boolean isLocalMethod(Method method) throws Throwable {
		Method[] methods = Mockable.class.getMethods();
		for(Method m : methods){
			if(m.equals(method)){						
				return true;
			}
		}
		return false;
	}
	
	protected Object invokeInternal(Method method){
		Queue<?> queue = map.get(method);
		Object o = null;
		if(queue == null || (o = queue.poll()) == null){
			t = new MockException("Unexpected method {" + method + "} called!");
		}else if(queue.size() == 0){
			map.remove(method);
		}
		if(o == NullObject.INSTANCE){
			return null;
		}
		return o;
	}
	
	public void replay(){
		replayed = true;
	}
	
	public void verify(){
		if(t != null){
			throw t;
		}else{
			for(Method m : map.keySet()){				
				throw new MockException("Expected method {" + m + "} uninvoked.");				
			}
		}
	}

	@Override
	public void andReturn(Object o) {
		if(o == null){
			o = NullObject.INSTANCE;
		}
		currentQueue.offer(o);	
	}

	static class VoidObject {
		static final VoidObject INSTANCE = new VoidObject();
	}

	static class NullObject{
		static final NullObject INSTANCE = new NullObject();
	}
}

