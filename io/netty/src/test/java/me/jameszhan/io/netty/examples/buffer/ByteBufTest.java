package me.jameszhan.io.netty.examples.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/9/29
 * Time: 上午10:52
 */
public class ByteBufTest {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    public void testBasic() {
        ByteBuf buf = Unpooled.buffer(256);
        Assert.assertEquals(0, buf.readerIndex());
        Assert.assertEquals(0, buf.writerIndex());
        Assert.assertEquals(256, buf.capacity());

        buf.writeCharSequence("Hello World!", UTF_8);
        Assert.assertEquals(0, buf.readerIndex());
        Assert.assertEquals(12, buf.writerIndex());
        Assert.assertEquals(256, buf.capacity());

        CharSequence str = buf.readCharSequence(5, UTF_8);
        Assert.assertEquals("Hello", str);
        Assert.assertEquals(5, buf.readerIndex());
        Assert.assertEquals(12, buf.writerIndex());
        Assert.assertEquals(256, buf.capacity());

        str = buf.readCharSequence(7, UTF_8);
        Assert.assertEquals(" World!", str);
        Assert.assertEquals(12, buf.readerIndex());
        Assert.assertEquals(12, buf.writerIndex());
        Assert.assertEquals(256, buf.capacity());

        buf.resetReaderIndex();
        str = buf.readCharSequence(12, UTF_8);
        Assert.assertEquals("Hello World!", str);
        Assert.assertEquals(12, buf.readerIndex());
        Assert.assertEquals(12, buf.writerIndex());
        Assert.assertEquals(256, buf.capacity());
    }
}
