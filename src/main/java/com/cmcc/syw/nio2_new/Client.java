package com.cmcc.syw.nio2_new;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO客户端
 * <p>
 * Created by sunyiwei on 2016/11/22.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        final String HOST = "localhost";
        final int PORT = 8888;

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_WRITE);
        while (true) {
            if (selector.select(1000) == 0) {
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                eventHandler(selectionKey, selector);

                iterator.remove();
            }
        }
    }

    private static void eventHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (selectionKey.isConnectable()) {
            if (sc.isConnectionPending()) {
                sc.finishConnect();

                //连接完成后马上发送一条消息
                String content = dateFormat.format(new Date()) + ":" + "hello, server.";
                sc.write(ByteBuffer.wrap(content.getBytes()));
            }

            sc.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            sc.read(byteBuffer);
            byteBuffer.flip();

            System.out.println(dateFormat.format(new Date()) + ":" + Charset.forName("utf-8").newDecoder().decode(byteBuffer).toString());
            sc.register(selector, SelectionKey.OP_WRITE);
        } else if (selectionKey.isWritable()) {
            String content = dateFormat.format(new Date()) + ":" + "hello, server.";
            sc.write(ByteBuffer.wrap(content.getBytes()));

            sc.register(selector, SelectionKey.OP_READ);
        }
    }
}
