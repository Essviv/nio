package com.cmcc.syw.reactor.singlethread.eventhandlers;

import java.nio.channels.SelectionKey;

/**
 * 事件处理器
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public interface EventHandler {
    void process(SelectionKey selectionKey);
}
