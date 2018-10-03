package me.jameszhan.io.netty.examples.http.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import io.netty.handler.codec.spdy.SpdyHttpHeaders.Names;

/**
 * Adds a unique client stream ID to the SPDY header. Client stream IDs MUST be odd.
 *
 * @author James Zhan
 * Date: 2018/9/29
 * Time: 上午10:08
 */
public class SpdyClientStreamIdHandler extends ChannelOutboundHandlerAdapter {

    private int currentStreamId = 1;

    public boolean acceptOutboundMessage(Object msg) {
        return msg instanceof HttpMessage;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (acceptOutboundMessage(msg)) {
            HttpMessage httpMsg = (HttpMessage) msg;
            if (!httpMsg.headers().contains(SpdyHttpHeaders.Names.STREAM_ID)) {
                httpMsg.headers().setInt(Names.STREAM_ID, currentStreamId);
                // Client stream IDs are always odd
                currentStreamId += 2;
            }
        }
        ctx.write(msg, promise);
    }
}
