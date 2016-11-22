package com.cmcc.syw.nio2_new;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * nio服务端
 * <p>
 * Created by sunyiwei on 2016/11/22.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        final String HOST = "localhost";
        final int PORT = 8888;

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(HOST, PORT));
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(1000) == 0) {
                continue;
            }

            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                //process
                eventHandler(selectionKey, selector);

                //remove
                iterator.remove();
            }
        }
    }

    private static void eventHandler(SelectionKey selectionKey, Selector selector) {
        if (!selectionKey.isValid()) {
            return;
        }

        try {
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);

                sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            } else if (selectionKey.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                socketChannel.read(byteBuffer);
                byteBuffer.flip();

                System.out.println(Charset.forName("utf-8").newDecoder().decode(byteBuffer).toString());
            } else if (selectionKey.isWritable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                String content = "hello world from server.";

                ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes());
                socketChannel.write(byteBuffer);
            }
        } catch (IOException e) {
            selectionKey.cancel();
            try {
                selectionKey.channel().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
