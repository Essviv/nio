package com.cmcc.syw.reactor.singlethread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Reactor模式的客户端代码
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class Client {
    private volatile boolean isRunning = true;

    private String name;

    public Client(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final int COUNT = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(COUNT);
        CountDownLatch cdl = new CountDownLatch(COUNT);

        for (int i = 0; i < COUNT; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        new Client(randStr(16)).go();
                        cdl.countDown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        cdl.await();
        executorService.shutdownNow();
    }

    private static String randStr(int length) {
        StringBuilder sb = new StringBuilder();

        Random r = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }

        return sb.toString();
    }

    public void go() throws IOException {
        final String HOST = "localhost";
        final int PORT = 8888;

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress(HOST, PORT));
        while (isRunning) {
            if (selector.select(1000) == 0) {
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isConnectable()) {
                    processConnect(selectionKey);
                } else if (selectionKey.isReadable()) {
                    processRead(selectionKey);
                } else if (selectionKey.isWritable()) {
                    processWrite(selectionKey);
                }

                iterator.remove();
            }
        }
    }

    private void processWrite(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        socketChannel.write(ByteBuffer.wrap(name.getBytes()));

        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
    }

    private void processRead(SelectionKey selectionKey) throws IOException {
        if (!selectionKey.isValid()) {
            return;
        }
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();

        System.out.println("Server responses: " + StandardCharsets.UTF_8.decode(byteBuffer));
        selectionKey.cancel();

        isRunning = false;
    }

    private void processConnect(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        if (socketChannel.isConnectionPending()) {
            socketChannel.finishConnect();


            //注册写事件
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_WRITE);
        }
    }
}
