package com.cmcc.syw.reactor.singlethread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * Reactor模式的客户端代码
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        final String HOST = "localhost";
        final int PORT = 8888;

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
            if (selector.select(1000) == 0) {
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isConnectable()) {
                    processConnect(selectionKey);
                } else if (selectionKey.isReadable()) {
                    processRead(selectionKey);
                }

                iterator.remove();
            }
        }
    }

    private static void processRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);

        System.out.println("Server responses: " + StandardCharsets.UTF_8.decode(byteBuffer));
        selectionKey.cancel();
    }

    private static void processConnect(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        if (socketChannel.isConnectionPending()) {
            socketChannel.finishConnect();

            final String content = "Hello, server";
            socketChannel.write(ByteBuffer.wrap(content.getBytes()));

            //注册读事件
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        }
    }
}
