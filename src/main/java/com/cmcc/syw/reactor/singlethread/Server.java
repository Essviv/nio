package com.cmcc.syw.reactor.singlethread;

import com.cmcc.syw.reactor.singlethread.eventhandlers.AcceptEventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * Reactor模型的入口类
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        Dispatcher dispatcher = new Dispatcher();

        final String HOST = "localhost";
        final int PORT = 8888;

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(HOST, PORT));
        ssc.configureBlocking(false);

        //register
        dispatcher.register(ssc, SelectionKey.OP_ACCEPT, new AcceptEventHandler(dispatcher));

        //start event loop
        Thread mainThread = new Thread(dispatcher);
        mainThread.start();

        //wait until event loop to end
        mainThread.join();
    }
}
