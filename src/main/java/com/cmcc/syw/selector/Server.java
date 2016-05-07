package com.cmcc.syw.selector;

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
 * Created by sunyiwei on 2016/5/3.
 */
public class Server {
    private static final int PORT = 12345;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(HOST, PORT));
        serverSocketChannel.configureBlocking(false);

        //register
        Selector selector = Selector.open();
        int insteresSet = SelectionKey.OP_ACCEPT;
        serverSocketChannel.register(selector, insteresSet);

        while (true) {
            if (selector.select() > 0) {
                Set<SelectionKey> selectionKeyset = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectionKeyset.iterator();

                while (iter.hasNext()) {
                    SelectionKey selectionKey = iter.next();

                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);

                        sc.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel sc = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

                        sc.read(byteBuffer);
                        byteBuffer.flip();

                        String content = Charset.forName("utf-8").newDecoder().decode(byteBuffer).toString();
                        String response = "你好, 客户端. 我已经收到你的消息, 内容为\"" + content + "\"";

                        sc.write(ByteBuffer.wrap(response.getBytes()));
                        sc.close();
                    } else {
                        System.out.println("Unknown op.");
                    }

                    iter.remove();
                }
            }
        }
    }
}
