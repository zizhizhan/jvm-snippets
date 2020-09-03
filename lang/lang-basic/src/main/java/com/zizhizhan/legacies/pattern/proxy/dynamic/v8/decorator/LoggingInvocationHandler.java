package com.zizhizhan.legacies.pattern.proxy.dynamic.v8.decorator;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class LoggingInvocationHandler<T> implements InvocationHandler {

	private T underlying;

	public LoggingInvocationHandler(T underlying) {
		this.underlying = underlying;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getName()).append("(");
		for (int i = 0; args != null && i < args.length; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(toString(args[i]));
		}
		sb.append(") ");

		try {
			Object ret = method.invoke(underlying, args);

			if (ret != null) {
				sb.append("-> ").append(toString(ret));
			}
			log.info("{}", sb);
			return ret;
		} catch (Throwable t) {
			sb.append("-> ").append(toString(t));
			log.info("{}", sb);
			throw t;
		}
	}
	
	
	private static String toString(Object o){
		Class<?> clazz = o.getClass();
		if(String.class.isAssignableFrom(clazz)){
			return "\"" + o + "\"";
		}
		Class<?>[] numericType = {Short.class, Integer.class, Long.class, Boolean.class};
		for(Class<?> type : numericType){
			if(type.isAssignableFrom(clazz)){
				return o.toString();
			}
		}
		if(Throwable.class.isAssignableFrom(clazz)) {
			Throwable t = (Throwable) o;
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			return sw.toString();			
		}
		if(clazz.isArray()){
			StringBuilder sb = new StringBuilder();
			Object[] arr = (Object[]) o;
			sb.append('[');		
			for(int i = 0; i < arr.length; i++){
				if(i != 0){
					sb.append(", ");
				}
				sb.append(toString(arr[i]));
			}
			sb.append(']');
			return sb.toString();
		}
		return o.toString();		
	}
	
	
}
