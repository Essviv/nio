package com.cmcc.syw.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.*;

/**
 * Created by sunyiwei on 16/5/12.
 */
public class CustomChannelInitializer extends ChannelInitializer<Channel> {
    private boolean isClient;

    public CustomChannelInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        if (isClient) {
            ch.pipeline().addLast(new HttpClientCodec());
            ch.pipeline().addLast(new HttpContentDecompressor());
        } else {
            ch.pipeline().addLast(new HttpServerCodec());
            ch.pipeline().addLast(new HttpContentCompressor());
        }

        ch.pipeline().addLast(new HttpObjectAggregator(16 * 1024));
        ch.pipeline().addLast(new CustomInboundHandler());
    }
}
