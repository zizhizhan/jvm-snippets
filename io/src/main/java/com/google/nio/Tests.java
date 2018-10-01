package com.google.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class Tests {
	
	
	
	public static void main(String[] args) throws IOException {
		
		
		FileChannel fc = new RandomAccessFile("nio.txt", "rw").getChannel();
		fc.position(fc.size());
		fc.write(ByteBuffer.wrap("Hello World!\n".getBytes("gb2312")));		
		//fc.close();
		
		
		
		//fc = new RandomAccessFile("nio.txt", "rw").getChannel();	
		fc.position(0);
		
		ByteBuffer bb = ByteBuffer.allocate(1024);
		
		fc.read(bb);
		bb.flip();
		while(bb.hasRemaining()){
			System.out.print((char)bb.get());
		}
		
		fc.close();
	
		
		
		
		
		
	}

}
