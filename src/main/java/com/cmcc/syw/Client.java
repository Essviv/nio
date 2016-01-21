package com.cmcc.syw;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO客户端
 * Created by sunyiwei on 16-1-21.
 */
public class Client {
    private final static InetSocketAddress SERVER_ADDRESS
            = new InetSocketAddress("localhost", 8888);

    private static final int BLOCK = 4096;
    private static ByteBuffer receivedBuffer = ByteBuffer.allocate(BLOCK);
    private static ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(SERVER_ADDRESS);

        SocketChannel client;

        while (true){
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isConnectable()){
                    System.out.println("Client connect.");
                    client = (SocketChannel)key.channel();
                    if(client.isConnectionPending()){
                        client.finishConnect();
                        System.out.println("完成连接!");

                        sendBuffer.clear();
                        String message = "Hello world from client";
                        sendBuffer.put(message.getBytes());
                        sendBuffer.flip();

                        client.write(sendBuffer);
                    }

                    client.register(selector, SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    client = (SocketChannel)key.channel();
                   receivedBuffer.clear();

                    int count =  0;
                    if((count = client.read(receivedBuffer)) > 0){
                        System.out.println(
                                "接收到来自服务端的消息: " + new String(receivedBuffer.array(), 0, count)
                        );

                        client.register(selector, SelectionKey.OP_WRITE);
                    }
                }else if(key.isWritable()){
                    sendBuffer.clear();
                    String message = "Hello world from client";
                    sendBuffer.put(message.getBytes());
                    sendBuffer.flip();

                    client = (SocketChannel)key.channel();
                    client.write(sendBuffer);
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
        }
    }
}
