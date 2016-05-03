package com.cmcc.syw.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * demo to use selector
 * <p>
 * Created by sunyiwei on 2016/5/3.
 */
public class Client {
    private static final int PORT = 12345;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        SocketChannel channel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        channel.configureBlocking(false);
        int interesSet = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
        channel.register(selector, interesSet);

        while (true) {
            int keys = selector.select();
            if (keys <= 0) {
                continue;
            }

            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> iter = keySet.iterator();
            while(iter.hasNext()){
                SelectionKey selectionKey = iter.next();

                if(selectionKey.isConnectable()){
                    System.out.println("Accept op is ready!");
                }else if(selectionKey.isReadable()){
                    System.out.println("Read op is ready.");
                }else if(selectionKey.isWritable()){
                    System.out.println("Write op is ready.");
                }else{
                    System.out.println("Unknown op is ready.");
                }

                iter.remove();
            }
        }
    }
}
