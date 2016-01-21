package com.cmcc.syw;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sunyiwei on 16-1-20.
 */
public class Server {
    private int port;
    private boolean isRunning;
    private Selector selector;

    private final int BLOCK = 4096;
    private ByteBuffer receivedBuffer = ByteBuffer.allocate(BLOCK);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);

    public Server(int port) {
        this.port = port;
        this.isRunning = true;

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));

        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void startListen() throws IOException {
        while (isRunning) {
            selector.select();

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                handler(selectionKey);
            }
        }

        selector.close();
    }

    private void handler(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel server = null;
        SocketChannel client = null;

        if (selectionKey.isAcceptable()) {
            server = (ServerSocketChannel) selectionKey.channel();
            client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            client = (SocketChannel) selectionKey.channel();
            receivedBuffer.clear();

            int count = 0;
            if ((count = client.read(receivedBuffer)) > 0) {
                System.out.println("服务端接收客户端发送数据:" + new String(receivedBuffer.array(), 0, count));
                client.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (selectionKey.isWritable()) {
            client = (SocketChannel) selectionKey.channel();
            sendBuffer.clear();

            String message = "hello world from client.";
            sendBuffer.put(message.getBytes());
            sendBuffer.flip();

            client.write(sendBuffer);
            System.out.println("服务端向客户端发送数据: " + message);
            client.register(selector, SelectionKey.OP_READ);
        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(8888);
        server.startListen();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
