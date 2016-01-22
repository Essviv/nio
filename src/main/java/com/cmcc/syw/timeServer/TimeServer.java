package com.cmcc.syw.timeServer;

import com.cmcc.syw.executors.ServerExecutor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class TimeServer {
    private final int WORKER_GROUP_SIZE = 100;
    private ExecutorService workerThreadGroup = Executors.newFixedThreadPool(WORKER_GROUP_SIZE);

    public ServerSocketChannel setup(int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));

        return ssc;
    }

    public void serve(final ServerSocketChannel ssc) throws IOException {
        final SocketChannel sc = ssc.accept();
        workerThreadGroup.execute(new ServerExecutor(sc));
    }

    public static void main(String[] args) {
        final int port = 8888;
        TimeServer timeServer = new TimeServer();

        try {
            ServerSocketChannel ssc = timeServer.setup(port);
            while (true) {
                timeServer.serve(ssc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
