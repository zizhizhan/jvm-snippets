package com.google.nio;

import java.util.concurrent.Executor;

public class ThreadExecutor implements Executor{
	
	@Override
	public void execute(Runnable command) {
		Thread t = new Thread(command);			
		t.start();	
		//command.run();
	}

}
