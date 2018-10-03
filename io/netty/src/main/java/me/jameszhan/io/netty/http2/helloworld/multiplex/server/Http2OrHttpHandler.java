package me.jameszhan.io.netty.http2.helloworld.multiplex.server;

import io.netty.channel.ChannelHandlerContext;
import me.jameszhan.io.netty.http2.helloworld.server.HelloWorldHttp1Handler;
import me.jameszhan.thirdparty.netty.http2.helloworld.server.HelloWorldHttp1Handler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http2.Http2MultiplexCodecBuilder;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;

/**
 * Negotiates with the browser if HTTP2 or HTTP is going to be used. Once decided, the Netty
 * pipeline is setup with the correct handlers for the selected protocol.
 *
 * @author James Zhan
 * Date: 2018/9/29
 * Time: 上午10:48
 */
public class Http2OrHttpHandler extends ApplicationProtocolNegotiationHandler {

    private static final int MAX_CONTENT_LENGTH = 1024 * 100;

    protected Http2OrHttpHandler() {
        super(ApplicationProtocolNames.HTTP_1_1);
    }

    @Override
    protected void configurePipeline(ChannelHandlerContext ctx, String protocol) throws Exception {
        if (ApplicationProtocolNames.HTTP_2.equals(protocol)) {
            ctx.pipeline().addLast(Http2MultiplexCodecBuilder.forServer(new HelloWorldHttp2Handler()).build());
            return;
        }

        if (ApplicationProtocolNames.HTTP_1_1.equals(protocol)) {
            ctx.pipeline().addLast(new HttpServerCodec(),
                    new HttpObjectAggregator(MAX_CONTENT_LENGTH),
                    new HelloWorldHttp1Handler("ALPN Negotiation"));
            return;
        }

        throw new IllegalStateException("unknown protocol: " + protocol);
    }
}
