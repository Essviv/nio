package com.cmcc.syw.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by sunyiwei on 16/5/12.
 */
public class NettyHttpServer {
    private static final int PORT = 8764;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws InterruptedException {
        final int COUNT = 100;
        EventLoopGroup elg = new NioEventLoopGroup(COUNT);
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(elg)
                .localAddress(new InetSocketAddress(HOST, PORT))
                .channel(NioServerSocketChannel.class)
                .childHandler(new CustomChannelInitializer(false));

        ChannelFuture f = serverBootstrap.bind().sync();
        f.channel().closeFuture().sync();
    }
}
