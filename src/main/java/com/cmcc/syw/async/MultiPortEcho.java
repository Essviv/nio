package com.cmcc.syw.async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sunyiwei on 16-1-24.
 */
public class MultiPortEcho {
    private int[] ports;
    private Selector selector;

    public MultiPortEcho(int[] ports) throws IOException {
        this.ports = ports;
        selector = Selector.open();

        init();
        process();
    }

    private void init() throws IOException {
        for (int port : ports) {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);

            ServerSocket sc = ssc.socket();
            sc.bind(new InetSocketAddress(port));
            System.out.println("Listen to port = " + port);

            ssc.register(selector, SelectionKey.OP_ACCEPT);
        }
    }

    private void process() throws IOException {
        while (true) {
            if(selector.select() == 0){
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();

            Iterator<SelectionKey> iter = keys.iterator();
            while (iter.hasNext()) {
                SelectionKey sk = iter.next();

                if (sk.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
                    SocketChannel sc = ssc.accept();
                    System.out.println("Connect form addr = " + sc.getRemoteAddress());
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                }else if(sk.isReadable()){
                    SocketChannel sc = (SocketChannel) sk.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    while(-1 != sc.read(byteBuffer)){
                        byteBuffer.flip();
                        sc.write(byteBuffer);
                        byteBuffer.clear();
                    }
                }else{
                    System.out.println("Uninterested event.");
                }

                iter.remove();
            }
        }
    }

    public static void main(String[] args) {
        int[] ports = new int[]{10000, 10001, 10002, 10003, 10004};

        try {
            new MultiPortEcho(ports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
