package me.jameszhan.io.framework.reactor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class FileTests {
	
	private static CyclicBarrier barrier = new CyclicBarrier(4, new Runnable() {		
		@Override
		public void run() {		
			System.out.println("barrier open!");
		}
	});	
	
	
	private static ExecutorService exec = Executors.newFixedThreadPool(6, new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {			
			return new Thread(r);
		}
		
	});
	
	public static void main(String[] args) throws IOException {
		
		//Selector selector = Selector.open();
		
		doCopy("1.txt");
		doCopy("2.txt");
		doCopy("3.txt");
		doCopy("4.txt");
		
		exec.shutdown();
		
	}
	
	
	static void doCopy(final String output){
		exec.execute(new Runnable() {			
			@Override
			public void run() {	
				try {
					barrier.await();
					System.out.println("begin");
				} catch (InterruptedException e) {					
					e.printStackTrace();
				} catch (BrokenBarrierException e) {					
					e.printStackTrace();
				}
				
				try {
					copy(output);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	
	
	
	public static void copy(String output) throws IOException{
		
		FileChannel in = new FileInputStream("nio.txt").getChannel(), 
			out = new FileOutputStream(output).getChannel();
		
		ByteBuffer buf = ByteBuffer.allocate(8);
		while(in.read(buf) != -1){
			buf.flip();
			out.write(buf);
			buf.clear();
		}
	}
	
	
	public static void copy1(String output) throws IOException{
		
		FileChannel in = new FileInputStream("nio.txt").getChannel(), 
			out = new FileOutputStream(output).getChannel();
		
		in.transferTo(0, in.size(), out);

	}

}
