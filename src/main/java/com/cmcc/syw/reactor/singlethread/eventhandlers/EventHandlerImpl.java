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
    private State readState = new ReadState(this);
    private State writeState = new WriteState();
    private State currentState = readState;

    public EventHandlerImpl(boolean runWithThreadPool) {
        this.runWithThreadPool = runWithThreadPool;
        this.currentState = readState;
    }

    @Override
    public void process(SelectionKey selectionKey) {
        //正在处理中
        if (currentState.isProcessing()) {
            return;
        }

        currentState.startProcessing();
        if (runWithThreadPool) {
            executorService.submit(() -> currentState.process(selectionKey));
        } else {
            currentState.process(selectionKey);
        }
    }

    //状态模式中的状态
    private interface State {
        void process(SelectionKey selectionKey);

        boolean isProcessing();

        void startProcessing();
    }

    private class WriteState implements State {
        private boolean isProcessing = false;

        @Override
        public void startProcessing() {
            isProcessing = true;
        }

        @Override
        public void process(SelectionKey selectionKey) {
            SocketChannel sc = (SocketChannel) selectionKey.channel();

            try {
                final String content = "Bye, " + clientName;
                sc.write(ByteBuffer.wrap(content.getBytes()));

                selectionKey.cancel();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                isProcessing = false;
            }
        }

        @Override
        public boolean isProcessing() {
            return isProcessing;
        }
    }

    private class ReadState implements State {
        private boolean isProcessing = false;
        private EventHandlerImpl eventHandler;

        public ReadState(EventHandlerImpl eventHandler) {
            this.eventHandler = eventHandler;
        }

        @Override
        public void startProcessing() {
            isProcessing = true;
        }

        @Override
        public void process(SelectionKey selectionKey) {
            SocketChannel sc = (SocketChannel) selectionKey.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            try {
                sc.read(byteBuffer);
                byteBuffer.flip();

                clientName = String.valueOf(StandardCharsets.UTF_8.decode(byteBuffer));
                System.out.println(Thread.currentThread().getName() + ": Client addr: " + sc.getRemoteAddress());

                Selector selector = selectionKey.selector();
                sc.register(selector, SelectionKey.OP_WRITE, this.eventHandler);
                currentState = writeState; //状态变迁

                selector.wakeup();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                isProcessing = false;
            }
        }

        @Override
        public boolean isProcessing() {
            return isProcessing;
        }
    }
}
