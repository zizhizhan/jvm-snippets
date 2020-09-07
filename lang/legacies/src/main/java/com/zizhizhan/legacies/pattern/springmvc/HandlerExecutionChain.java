package com.zizhizhan.legacies.pattern.springmvc;


public class HandlerExecutionChain {
	
	private final Object handler;
	
	/**
	 * Create a new HandlerExecutionChain.
	 * @param handler the handler object to execute
	 */
	public HandlerExecutionChain(Object handler) {
		if (handler instanceof HandlerExecutionChain) {
			HandlerExecutionChain originalChain = (HandlerExecutionChain) handler;
			this.handler = originalChain.getHandler();			
		} else {
			this.handler = handler;			
		}
	}
	
	/**
	 * Return the handler object to execute.
	 * @return the handler object
	 */
	public Object getHandler() {
		return this.handler;
	}

}
