package com.cmcc.syw.netty;

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
 * nioClient example
 * <p>
 * Created by sunyiwei on 16/5/9.
 */
public class NIOClient {
    private final static String HOST = "localhost";
    private final static int PORT = 3432;

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        sc.configureBlocking(false);

        Selector selector = Selector.open();
        sc.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);


        boolean isRunning = true;
        int count = 0;
        while (isRunning) {
            if (selector.select() == 0) {
                continue;
            }

            Set<SelectionKey> keySet = selector.selectedKeys();

            Iterator<SelectionKey> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey sk = iterator.next();
                SelectionKey tmp = null;

                SocketChannel socketChannel = (SocketChannel) sk.channel();

                if (sk.isWritable()) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap("dkflsjla".getBytes());

                    if (count++ > 10) {
                        socketChannel.close();
                        isRunning = false;
                        continue;
                    }

                    while (byteBuffer.hasRemaining()) {
                        socketChannel.write(byteBuffer);
                    }
                } else if (sk.isReadable()) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    socketChannel.read(byteBuffer);
                    StringBuilder sb = new StringBuilder();
                    while (byteBuffer.hasRemaining()) {
                        sb.append(StandardCharsets.UTF_8.newDecoder().decode(byteBuffer));
                    }

                    String content = sb.toString();
                    System.out.println(content);
                }

                iterator.remove();
            }
        }
    }
}
