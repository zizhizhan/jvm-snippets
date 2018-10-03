package me.jameszhan.io.util;

import java.util.concurrent.Executor;

public class ThreadExecutor implements Executor{

	@Override
	public void execute(Runnable command) {
		new Thread(command).start();
	}

}
