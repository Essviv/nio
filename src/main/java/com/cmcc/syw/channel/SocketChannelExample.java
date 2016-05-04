package com.cmcc.syw.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by sunyiwei on 2016/5/4.
 */
public class SocketChannelExample {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        socketChannel.connect(new InetSocketAddress("localhost", 12345));
        while(!socketChannel.finishConnect()){

        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while(byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
    }
}
