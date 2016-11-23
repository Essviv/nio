package com.cmcc.syw.reactor.singlethread;

import com.cmcc.syw.reactor.singlethread.eventhandlers.EventHandler;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * 分发器
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class Dispatcher implements Runnable {
    //synchronous event demultiplexer
    private Selector selector;

    private boolean isRunning;

    public Dispatcher() {
        try {
            selector = Selector.open();
            isRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isRunning = false;
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
        //event loop
        while (isRunning) {
            try {
                //阻塞, 监听事件的到达
                if (selector.select(1000) > 0) {
                    //事件分发
                    Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if(!selectionKey.isValid()){
                            continue;
                        }

                        EventHandler eventHandler = (EventHandler) selectionKey.attachment();
                        eventHandler.set(selectionKey);

                        //具体的事件交由相应的处理器进行处理
                        new Thread(eventHandler).start();

                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void register(SelectableChannel channel, int ops, EventHandler eventHandler) {
        try {
            channel.register(selector, ops, eventHandler);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }
}
