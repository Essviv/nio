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

    private static int count = 0;

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

        if (selectionKey.isReadable()) {
            SocketChannel sc = (SocketChannel) selectionKey.channel();

            if (!selectionKey.isValid()) {
                return;
            }

            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            try {
                sc.read(byteBuffer);
                byteBuffer.flip();

                String clientName = String.valueOf(StandardCharsets.UTF_8.decode(byteBuffer));
                System.out.println("Client name: " + clientName);

                dispatcher.register(sc, SelectionKey.OP_WRITE, new WriterEventHandlerImpl(clientName, dispatcher));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return ReadEventHandler.class.getSimpleName();
    }
}
