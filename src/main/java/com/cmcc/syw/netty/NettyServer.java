package com.cmcc.syw.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by sunyiwei on 16/5/9.
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        final int COUNT = 100;
        final int PORT = 2342;

        EventLoopGroup elg = new NioEventLoopGroup(COUNT);

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(elg)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("Channel registered.");
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("Channel active.");
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println("Server received: " + msg);
                                ctx.write(msg);
                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                ctx.close();
                            }
                        });
                    }
                }).localAddress(new InetSocketAddress(PORT));

        //bind
        try {
            ChannelFuture f = bootstrap.bind().sync();
            System.out.format("Server start to listen on %s. %n", f.channel().localAddress());

            //close
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            elg.shutdownGracefully().sync();
        }
    }
}
