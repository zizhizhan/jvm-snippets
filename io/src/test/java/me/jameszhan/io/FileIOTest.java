package me.jameszhan.io;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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


}
