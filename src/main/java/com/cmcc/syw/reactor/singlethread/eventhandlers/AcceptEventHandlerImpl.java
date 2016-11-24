package com.cmcc.syw.reactor.singlethread.eventhandlers;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 接收连接的事件处理器
 * <p>
 * Created by sunyiwei on 2016/11/24.
 */
public class AcceptEventHandlerImpl implements EventHandler {
    private boolean runWithThreadPool = false;

    public AcceptEventHandlerImpl(boolean runWithThreadPool) {
        this.runWithThreadPool = runWithThreadPool;
    }

    @Override
    public void process(SelectionKey selectionKey) {
        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
        try {
            SocketChannel sc = ssc.accept();

            if (sc != null) {
                sc.configureBlocking(false);
                sc.register(selectionKey.selector(), SelectionKey.OP_READ, new EventHandlerImpl(runWithThreadPool));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
