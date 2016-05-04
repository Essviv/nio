package com.cmcc.syw.selector;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

/**
 * demo to use selector
 * <p>
 * Created by sunyiwei on 2016/5/3.
 */
public class Client {
    //server port
    private int port;

    //server host
    private String host;

    //client name
    private String name;

    //socket channel
    private SocketChannel channel;

    //worker
    private Worker worker;

    private Timer timer;

    private boolean isRunning;


    public void sendMessage(String message) throws IOException {
        message = name + ": " + message;
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes());

        channel.write(byteBuffer);
    }

    public void close() throws IOException {
        if (worker != null) {
            worker.close();
        }

        channel.close();

        timer.cancel();

        isRunning = false;
    }

    public Client(String host, int port, String name) throws IOException {
        this.host = host;
        this.port = port;
        this.name = name;

        init();

        //heart beat
        setHeartBeat();
    }

    private void setHeartBeat() {
        timer = new Timer("heartBeat");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    channel.write(ByteBuffer.wrap("ping".getBytes()));
                } catch (IOException e) {
                    try {
                        close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }, 1000, 1000);
    }

    private void init() throws IOException {
        Selector selector = Selector.open();

        channel = SocketChannel.open(new InetSocketAddress(host, port));
        channel.configureBlocking(false);

        int interesSet = SelectionKey.OP_READ;
        channel.register(selector, interesSet);

        //run a worker thread
        worker = new Worker(selector);
        new Thread(worker, name).start();

        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private class Worker implements Runnable {
        private Selector selector;

        //running flag
        private volatile boolean isRunning = false;

        public Worker(Selector selector) {
            this.selector = selector;

            isRunning = true;
        }

        public void close() {
            isRunning = false;
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    if (selector.select(1000) > 0 && channel.isOpen()) {
                        Set<SelectionKey> keySet = selector.selectedKeys();
                        Iterator<SelectionKey> iter = keySet.iterator();

                        //iter over selectionKeys
                        while (iter.hasNext()) {
                            SelectionKey selectionKey = iter.next();

                            if (selectionKey.isReadable()) {
                                SocketChannel sc = (SocketChannel) selectionKey.channel();

                                //read buffer
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                sc.read(byteBuffer);

                                //flip
                                byteBuffer.flip();

                                String content = Charset.forName("utf-8").newDecoder().decode(byteBuffer).toString();
                                System.out.println("收到服务端的消息: " + content);
                            }

                            //remove this selection key
                            iter.remove();
                        }
                    }
                } catch (Exception e) {
                    close();
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        final String HOST = "localhost";
        final int PORT = 12345;

        Scanner sc = new Scanner(System.in);

        System.out.print("请输入你的名字: ");

        String name = sc.nextLine();

        Client client = new Client(HOST, PORT, name);

        String content;
        while (StringUtils.isNotBlank(content = sc.nextLine()) && client.isRunning()) {
            client.sendMessage(content);
        }

        client.close();
    }
}
