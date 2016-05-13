package com.cmcc.syw.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * nioServer example
 * <p>
 * Created by sunyiwei on 16/5/9.
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        final int PORT = 3432;
        final String HOST = "localhost";

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(HOST, PORT));
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        SelectionKey tmp = null;

        while (true) {
            if(selector.select()==0){
                continue;
            }

            Set<SelectionKey> selectionKeyset = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeyset.iterator();

            while (iter.hasNext()) {
                SelectionKey sk = iter.next();

                if (sk.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) sk.channel();
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);

                    sc.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    SocketChannel sc = (SocketChannel) sk.channel();
                    if(!sc.isOpen()){
                        continue;
                    }

                    byteBuffer.clear();

                    sc.read(byteBuffer);
                    byteBuffer.flip();

                    StringBuilder sb = new StringBuilder();
                    while (byteBuffer.hasRemaining()) {
                        sb.append(StandardCharsets.UTF_8.newDecoder().decode(byteBuffer));
                    }
                    String content = sb.toString();
                    System.out.println(content);

                    byteBuffer.clear();
                    byteBuffer.put("ok".getBytes());

                    tmp = sc.register(selector, SelectionKey.OP_WRITE);
                } else if (sk.isWritable()) {
                    SocketChannel sc = (SocketChannel) sk.channel();

                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        sc.write(byteBuffer);
                    }

                    if (tmp != null) {
                        tmp.cancel();
                        tmp = null;
                    }
                }


                iter.remove();
            }
        }
    }
}
