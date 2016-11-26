package com.cmcc.syw.netty.channel;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by sunyiwei on 2016/11/26.
 */
public class CustomThread extends Thread {
    //要执行的任务
    private Queue<Runnable> tasks = new LinkedList<>();

    public void addTask(Runnable task) {
        tasks.offer(task);
    }
}
