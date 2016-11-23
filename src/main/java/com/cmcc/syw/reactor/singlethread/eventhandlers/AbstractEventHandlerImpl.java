package com.cmcc.syw.reactor.singlethread.eventhandlers;

import com.cmcc.syw.reactor.singlethread.Dispatcher;

import java.nio.channels.SelectionKey;

/**
 * 抽象的事件处理器
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public abstract class AbstractEventHandlerImpl implements EventHandler {
    protected Dispatcher dispatcher;
    private SelectionKey selectionKey;

    public AbstractEventHandlerImpl(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public SelectionKey get() {
        return selectionKey;
    }

    @Override
    public void set(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }
}
