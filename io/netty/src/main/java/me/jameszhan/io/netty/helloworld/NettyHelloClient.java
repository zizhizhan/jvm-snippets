package me.jameszhan.io.netty.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 11/28/18
 * Time: 4:02 PM
 */
public class NettyHelloClient {

    public static void main(String[] args) throws Exception {
        connect("www.baidu.com", 80);
    }

    public static void connect(String host, int port) throws InterruptedException {
        // 创建Reactor线程池，用来处理io请求，默认线程个数为内核cpu*2
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建启动类Bootstrap实例，用来设置客户端相关参数
            Bootstrap b = new Bootstrap()
                    .group(group)  // 设置线程池
                    .channel(NioSocketChannel.class)    // 指定用于创建客户端NIO通道的Class对象
                    .option(ChannelOption.TCP_NODELAY, true)    // 设置客户端套接字参数
                    .handler(new MyChannelInitializer()); // 设置用户自定义handler

            // 启动链接
            ChannelFuture f = b.connect(host, port).sync();

            // 同步等待链接断开
            f.channel().closeFuture().sync();
        } finally {
            // 优雅关闭线程池
            group.shutdownGracefully();
        }
    }

    public static class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) {
            ChannelPipeline p = ch.pipeline();
            //p.addLast(new NettyClientHandler());
        }
    }

}
