package com.zizhizhan.framework.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM11:08
 * To change this template use File | Settings | File Templates.
 */
public class MockObject implements InvocationHandler, Mockable {

	private final Map<Method, Queue<Object>> map = new LinkedHashMap<>();
	private MockException t;
	private boolean replayed;
	private Queue<Object> currentQueue;

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

	public void andReturn(Object o) {
		if(o == null){
			o = NullObject.INSTANCE;
		}
		currentQueue.offer(o);
	}
}

