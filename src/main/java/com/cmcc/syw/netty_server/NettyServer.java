package com.cmcc.syw.netty_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sunyiwei on 2016/11/28.
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        final byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};

        EventLoopGroup bossEventLoop = new NioEventLoopGroup(100, new ThreadFactory() {
            private AtomicInteger atomicInteger = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ServerEventGroup_" + String.valueOf(atomicInteger.getAndIncrement()));
            }
        });

        EventLoopGroup workerEventLoop = new NioEventLoopGroup(100, new ThreadFactory() {
            private AtomicInteger atomicInteger = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ClientEventGroup_" + String.valueOf(atomicInteger.getAndIncrement()));
            }
        });


        ServerBootstrap serverBootstrap = new ServerBootstrap()
            .group(bossEventLoop, workerEventLoop)
            .channel(NioServerSocketChannel.class)
            .localAddress("localhost", 8080)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpServerCodec());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            if (msg instanceof HttpRequest) {
                                FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(CONTENT));
                                httpResponse.headers().set("Content-Type", "text/plain");
                                httpResponse.headers().set("Content-Length", httpResponse.content().readableBytes());

                                ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
                            }
                        }

                        @Override
                        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                            ctx.flush();
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            cause.printStackTrace();
                            ctx.close();
                        }
                    });
                }
            });

        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        channelFuture.channel().closeFuture().sync();
    }
}
