package com.zizhizhan.legacy.pattern.interceptor;

public class Context {
	
	public void process(){
		System.out.println("Processing Context...");
	}
	
	public static void main(String[] args) {
		Context ctx = new Context();
		Dispatcher dispatcher = new ConcreteDispatcher(ctx);
		dispatcher.registerInterceptor(new Interceptor() {

			public void intercept(Context ctx) {
				System.out.println("Begin Testing.");
				ctx.process();
				System.out.println("End Testing.");
			}
		});
		
		dispatcher.registerInterceptor(new Interceptor() {

			public void intercept(Context ctx) {
				System.out.println("Begin Testing.");
				ctx.process();
				System.out.println("End Testing.");
			}
		});
		dispatcher.dispatch();
	}

}
