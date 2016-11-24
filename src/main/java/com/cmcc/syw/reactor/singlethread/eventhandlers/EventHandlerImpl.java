package com.cmcc.syw.reactor.singlethread.eventhandlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 事件处理器
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class EventHandlerImpl implements EventHandler {
    private static ExecutorService executorService = Executors.newFixedThreadPool(1000);

    private String clientName = null;
    private boolean runWithThreadPool = false;
    private Status status;

    public EventHandlerImpl(boolean runWithThreadPool) {
        this.runWithThreadPool = runWithThreadPool;
        this.status = Status.READ;
    }

    @Override
    public void process(SelectionKey selectionKey) {
        if (status == Status.READ) {
            status = Status.PROCESSING;

            if (runWithThreadPool) {
                executorService.submit(() -> processRead(selectionKey));
            } else {
                processRead(selectionKey);
            }
        } else if (status == Status.WRITE) {
            processWrite(selectionKey);
        }
    }

    private void processWrite(SelectionKey selectionKey) {
        SocketChannel sc = (SocketChannel) selectionKey.channel();

        try {
            final String content = "Bye, " + clientName;
            sc.write(ByteBuffer.wrap(content.getBytes()));

            selectionKey.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processRead(SelectionKey selectionKey) {
        SocketChannel sc = (SocketChannel) selectionKey.channel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        try {
            sc.read(byteBuffer);
            byteBuffer.flip();

            clientName = String.valueOf(StandardCharsets.UTF_8.decode(byteBuffer));
            System.out.println(Thread.currentThread().getName() + ": Client addr: " + sc.getRemoteAddress());

            Selector selector = selectionKey.selector();
            sc.register(selector, SelectionKey.OP_WRITE, this);
            status = Status.WRITE;

            selector.wakeup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum Status {
        READ,
        PROCESSING,
        WRITE
    }
}
