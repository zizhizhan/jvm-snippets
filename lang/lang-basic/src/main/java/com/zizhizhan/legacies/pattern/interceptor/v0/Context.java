package com.zizhizhan.legacies.pattern.interceptor.v0;

public class Context {
	
	public void process(){
		System.out.println("Processing Context...");
	}
	
	public static void main(String[] args) {
		Context ctx = new Context();
		Dispatcher dispatcher = new ConcreteDispatcher(ctx);
		dispatcher.registerInterceptor(ctx1 -> {
			System.out.println("Begin Testing.");
			ctx1.process();
			System.out.println("End Testing.");
		});
		
		dispatcher.registerInterceptor(ctx12 -> {
			System.out.println("Begin Testing.");
			ctx12.process();
			System.out.println("End Testing.");
		});

		dispatcher.dispatch();
	}

}
