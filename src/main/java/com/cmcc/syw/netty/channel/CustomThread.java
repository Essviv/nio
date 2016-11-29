package com.cmcc.syw.netty.channel;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程
 * <p>
 * Created by sunyiwei on 2016/11/26.
 */
public class CustomThread extends Thread {
    //要执行的任务
    private BlockingDeque<Runnable> tasks = new LinkedBlockingDeque<>();

    private volatile boolean isRuning = true;

    public CustomThread(String name) {
        super(name);

        isRuning = true;
        start();
    }

    public void stopRun() {
        isRuning = false;
    }

    public void addTask(Runnable task) {
        try {
            tasks.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isCurrentThread() {
        return Thread.currentThread() == this;
    }

    @Override
    public void run() {
        Runnable runnable = null;
        try {
            while (isRuning) {
                if ((runnable = tasks.poll(100, TimeUnit.MILLISECONDS)) != null) {
                    runnable.run();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
