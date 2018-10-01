package me.jameszhan.io;

import me.jameszhan.io.util.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.util.concurrent.Semaphore;

public class PipeTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PipeTest.class);

	@Test
	public void testSourceSink() throws IOException {
		Pipe pipe = Pipe.open();
		SinkChannel sink = pipe.sink(); // 写管道
		SourceChannel src = pipe.source(); // 读管道

		src.configureBlocking(false);
		sink.configureBlocking(false);

		String msg = "java.io.File, Hello World, Java - io/src/main/java/me/jameszhan/io";
		byte[] bytes = msg.getBytes(IOUtils.UTF_8);
		sink.write(ByteBuffer.wrap(bytes));

		ByteBuffer buffer = ByteBuffer.allocate(80);
		int length = src.read(buffer);
		Assert.assertEquals(msg.length(), length);

		buffer.flip();
		Assert.assertEquals(msg, new String(buffer.array(), buffer.position(), buffer.limit(), IOUtils.UTF_8));
	}

	@Test
	public void produceConsume() throws Exception {
		Pipe pipe = Pipe.open();
		SourceChannel sourceChannel = pipe.source();
		SinkChannel sinkChannel = pipe.sink();

		Semaphore empty = new Semaphore(1);
		Semaphore full = new Semaphore(0);
		new Thread(() -> {
			try {
				for (int i = 0; i < 100; i++) {
					empty.acquire();
					sinkChannel.write(IOUtils.UTF_8.encode(String.format("%02d: Hello World!", i)));
					full.release();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.close(sinkChannel);
			}
		}).start();

		try {
			ByteBuffer buffer = ByteBuffer.allocate(80);
			for (int i = 0; i < 100; i++) {
				full.acquire();
				buffer.clear();
				sourceChannel.read(buffer);
				buffer.flip();
				LOGGER.info("{} -> {}", i, new String(buffer.array(), buffer.position(), buffer.limit(), IOUtils.UTF_8));
				empty.release();
			}
		} finally {
			IOUtils.close(sourceChannel);
		}
	}
}
