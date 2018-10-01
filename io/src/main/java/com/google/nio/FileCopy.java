package com.google.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.nio.charset.Charset;



public class FileCopy {
	
	public static void main(String[] args) throws IOException {
		
		//Selector sel = Selector.open();
		
		Pipe pipe = Pipe.open();
		
		SourceChannel src = pipe.source();
		SinkChannel sink = pipe.sink();
		
		int i = 0;
		while(i++ < 100){
			src.read(Charset.defaultCharset().encode("Hello World " + i + "!"));
			ByteBuffer buf = ByteBuffer.allocate(80);
			sink.write(buf);
			
			System.out.println(Charset.defaultCharset().decode(buf));
		}
		
	}

}
