package me.jameszhan.io.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/8
 *         Time: PM11:41
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("Server say : " + msg);

        if ("ping".equals(msg)) {
            ctx.channel().writeAndFlush("OK\n");
        } else {
            //业务逻辑
        }
    }
}