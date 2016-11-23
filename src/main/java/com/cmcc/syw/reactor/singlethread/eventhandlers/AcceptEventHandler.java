package com.cmcc.syw.reactor.singlethread.eventhandlers;

import com.cmcc.syw.reactor.singlethread.Dispatcher;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 接收连接的事件处理器
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class AcceptEventHandler extends AbstractEventHandlerImpl {
    public AcceptEventHandler(Dispatcher dispatcher) {
        super(dispatcher);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        SelectionKey selectionKey = get();
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
            try {
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);

                dispatcher.register(sc, SelectionKey.OP_READ, new ReadEventHandler(dispatcher));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
