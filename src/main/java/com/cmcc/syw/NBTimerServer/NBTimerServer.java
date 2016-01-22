package com.cmcc.syw.NBTimerServer;

import com.cmcc.syw.executors.ServerExecutor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class NBTimerServer {
    private final int WORKER_GROUP_SIZE = 100;
    private ExecutorService workerThreadGroup = Executors.newFixedThreadPool(WORKER_GROUP_SIZE);
    private Selector selector;

    public NBTimerServer() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup(int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void serve() throws IOException {
        if (selector.select() > 0) {
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey sk = keys.next();
                keys.remove();

                ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
                final SocketChannel sc = ssc.accept();
                workerThreadGroup.execute(new ServerExecutor(sc));
            }
        }
    }

    public static void main(String[] args) {
        final int port = 8888;
        NBTimerServer timeServer = new NBTimerServer();

        try {
            timeServer.setup(port);
            while (true) {
                timeServer.serve();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
