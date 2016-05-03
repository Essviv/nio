package com.cmcc.syw.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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

        Selector selector = Selector.open();
        int insteresSet = SelectionKey.OP_ACCEPT;

        serverSocketChannel.register(selector, insteresSet);

        while(true){
            int keys = selector.select();
            if(keys <= 0){
                continue;
            }

            Set<SelectionKey> selectionKeyset = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeyset.iterator();
            while(iter.hasNext()){
                SelectionKey selectionKey = iter.next();

                if(selectionKey.isAcceptable()){
                    System.out.println("Accep op is ready.");
                }else if(selectionKey.isReadable()){
                    System.out.println("Read op is ready.");
                }else if(selectionKey.isWritable()){
                    System.out.println("Write op is ready.");
                }else{
                    System.out.println("Unknown op.");
                }

                iter.remove();
            }
        }
    }
}
