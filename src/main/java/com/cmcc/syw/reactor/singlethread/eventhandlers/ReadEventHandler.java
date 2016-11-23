package com.cmcc.syw.reactor.singlethread.eventhandlers;

import com.cmcc.syw.reactor.singlethread.Dispatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 读事件处理器
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class ReadEventHandler extends AbstractEventHandlerImpl {

    public ReadEventHandler(Dispatcher dispatcher) {
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
        if (!selectionKey.isValid()) {
            return;
        }

        if (selectionKey.isReadable()) {
            SocketChannel sc = (SocketChannel) selectionKey.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            try {
                sc.read(byteBuffer);
                System.out.println(String.valueOf(StandardCharsets.UTF_8.decode(byteBuffer)));

                dispatcher.register(sc, SelectionKey.OP_WRITE, new WriterEventHandlerImpl(dispatcher));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
