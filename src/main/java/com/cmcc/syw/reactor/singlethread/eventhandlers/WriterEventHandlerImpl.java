package com.cmcc.syw.reactor.singlethread.eventhandlers;

import com.cmcc.syw.reactor.singlethread.Dispatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 写事件处理器
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class WriterEventHandlerImpl extends AbstractEventHandlerImpl {
    private String name;

    public WriterEventHandlerImpl(String name, Dispatcher dispatcher) {
        super(dispatcher);

        this.name = name;
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
        if (!selectionKey.isValid()) {
            return;
        }

        SocketChannel sc = (SocketChannel) selectionKey.channel();

        try {
            final String content = "Bye, " + name;
            sc.write(ByteBuffer.wrap(content.getBytes()));

            selectionKey.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return WriterEventHandlerImpl.class.getSimpleName();
    }
}
