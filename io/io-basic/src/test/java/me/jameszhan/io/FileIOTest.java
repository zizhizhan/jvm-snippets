package me.jameszhan.io;

import me.jameszhan.io.util.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 * Date: 2018/9/17
 * Time: 下午4:27
 */
public class FileIOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileIOTest.class);

    @Test
    public void testChannel() throws IOException {
        String originFile = "/usr/local/rvm/archives/ruby-2.5.1.tar.bz2";
        long timeStart = System.currentTimeMillis();
        try (FileChannel out = new FileOutputStream("/tmp/test").getChannel()) {
            try (FileChannel in = new FileInputStream(originFile).getChannel()) {
                in.transferTo(0, in.size(), out);
                LOGGER.info("TestChannel Copy file cost {}ms.", System.currentTimeMillis() - timeStart);
            }
        }
    }

    @Test
    public void testBlock() throws IOException {
        long timeStart = System.currentTimeMillis();
        byte[] buf = new byte[8192];
        int len;
        String originFile = "/usr/local/rvm/archives/ruby-2.5.1.tar.bz2";
        try (FileOutputStream out = new FileOutputStream("/tmp/test2")) {
            try (FileInputStream in = new FileInputStream(originFile)) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                LOGGER.info("TestBlock Copy file cost {}ms.", System.currentTimeMillis() - timeStart);
            }
        }
    }

    @Test
    public void randomAccessFile() throws IOException {
        String filePath = "/tmp/nio.txt";
        new File(filePath).delete();

        String message = "Hello World!\n";
        try (FileChannel fc = new RandomAccessFile(filePath, "rw").getChannel()) {
            fc.position(fc.size());
            fc.write(ByteBuffer.wrap(message.getBytes(IOUtils.UTF_8)));

            fc.position(0);
            ByteBuffer bb = ByteBuffer.allocate(1024);
            fc.read(bb);
            bb.flip();

            Assert.assertEquals(message, new String(bb.array(), bb.position(), bb.limit(), IOUtils.UTF_8));
        }
    }
}