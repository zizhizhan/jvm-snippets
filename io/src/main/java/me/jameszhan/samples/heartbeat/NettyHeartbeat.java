package me.jameszhan.samples.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/11
 *         Time: AM12:41
 */
public class NettyHeartbeat {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyHeartbeat.class);

    public static void main(String[] args) {
        int port = 6666;
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new LoggingHandler(LogLevel.INFO),
                                new IdleStateHandler(30, 0, 0),// 心跳控制
                                new ServerHandler());
                    }
                });

            // Start the server.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException {}.", e);
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    static class ServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            LOGGER.info("channelRead context: {}, message: {}.", ctx.name(), msg);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            LOGGER.info("userEventTriggered context: {}, event: {}.", ctx.name(), evt);
            /*心跳处理*/
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE) {
                    /*读超时*/
                    System.out.println("READER_IDLE 读超时");
                    ctx.disconnect();
                } else if (event.state() == IdleState.WRITER_IDLE) {
                    /*写超时*/
                    System.out.println("WRITER_IDLE 写超时");
                } else if (event.state() == IdleState.ALL_IDLE) {
                    /*总超时*/
                    System.out.println("ALL_IDLE 总超时");
                }
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            LOGGER.info("channelInactive context: {}.", ctx.name());
            // 这里加入玩家的掉线处理
            ctx.close();
        }
    }

}
