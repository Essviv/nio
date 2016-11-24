package com.cmcc.syw.reactor.singlethread.eventhandlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
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
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private boolean isProcessing = false;
    private String clientName = null;
    private boolean runWithThreadPool = false;

    public EventHandlerImpl(boolean runWithThreadPool) {
        this.runWithThreadPool = runWithThreadPool;
    }

    @Override
    public void process(SelectionKey selectionKey) {
        if (isProcessing) {
            return;
        }

        if (selectionKey.isReadable()) {
            synchronized (this) {
                if (isProcessing) {
                    return;
                }

                isProcessing = true;
            }

            if (runWithThreadPool) {
                executorService.submit(() -> processRead(selectionKey));
            } else {
                processRead(selectionKey);
            }
        } else if (selectionKey.isWritable()) {
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

            sc.register(selectionKey.selector(), SelectionKey.OP_WRITE, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        isProcessing = false;
    }
}
