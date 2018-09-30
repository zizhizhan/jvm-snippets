package me.jameszhan.io.net.select;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/30
 * Time: 下午8:32
 */
public final class IOUtils {
    private IOUtils() {}

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    public static String readFully(ByteBuffer buffer, ReadableByteChannel channel, boolean hasFragmentation) throws IOException {
        int length, total = 0;
        try {
            if (hasFragmentation) {
                while ((length = channel.read(buffer)) > 0) {
                    total += length;
                    if (!buffer.hasRemaining()) {
                        break;
                    }
                }
            } else {
                length = channel.read(buffer);
                if (length > 0) {
                    total = length;
                }
            }
        } finally {
            buffer.flip();
        }
        if (total > 0) {
            LOGGER.info("Read \n{}\n", new String(buffer.array(), buffer.position(), total, UTF_8));
            return UTF_8.decode(buffer).toString();
        } else {
            return null;
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.info("Ignore close error.", e);
        }
    }

}
