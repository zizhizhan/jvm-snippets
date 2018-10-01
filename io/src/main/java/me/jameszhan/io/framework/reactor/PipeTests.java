package me.jameszhan.io.framework.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;

public class PipeTests {
	
	public static void main(String[] args) throws IOException{
		
		Pipe pipe = Pipe.open();
		SinkChannel sink = pipe.sink();
		SourceChannel src = pipe.source();
		
		src.configureBlocking(false);
		sink.configureBlocking(false);
		
		String msg = "java.io.File, Hello World, Java - thoery/src/com/google/nio";		
		src.read(ByteBuffer.wrap(msg.getBytes()));
	
		
		ByteBuffer buf = ByteBuffer.allocate(80);
		sink.write(buf);
		
		buf.flip();
		while(buf.hasRemaining()){
			System.out.println((char)buf.get());
		}
		
		
		
		
		
	}

}
