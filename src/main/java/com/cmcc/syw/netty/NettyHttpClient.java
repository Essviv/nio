package com.cmcc.syw.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by sunyiwei on 16/5/12.
 */
public class NettyHttpClient {
    private static final int PORT = 8764;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws InterruptedException {
        final int COUNT = 100;
        EventLoopGroup elg = new NioEventLoopGroup(COUNT);
        Bootstrap bootstrap = new Bootstrap()
                .group(elg)
                .remoteAddress(new InetSocketAddress(HOST, PORT))
                .channel(NioSocketChannel.class)
                .handler(new CustomChannelInitializer(true));

        ChannelFuture f = bootstrap.connect().sync();
        f.channel().closeFuture().sync();
    }
}
