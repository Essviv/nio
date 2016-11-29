package com.cmcc.syw.netty.channel;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 模拟jetty中的channel
 * <p>
 * 它有个特点, 在创建的时候会被指定相应的线程对象,后续这个channel所有的操作都会在这个线程中执行
 * Created by sunyiwei on 2016/11/26.
 */
public class CustomChannel {
    //当前channel指定的线程对象
    private final CustomThread thread;
    private final String name;

    public CustomChannel(String name, CustomThread thread) {
        this.name = name;
        this.thread = thread;
    }

    public static void main(String[] args) {
        final int THREAD_COUNT = 10;
        List<CustomThread> threads = new LinkedList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads.add(new CustomThread(CustomThread.class.getSimpleName() + "_" + i));
        }

        final int COUNT = 1000;
        List<CustomChannel> chs = new LinkedList<>();
        for (int i = 0; i < COUNT; i++) {
            chs.add(new CustomChannel("CustomChannel_" + i, threads.get(i % 10)));
        }

        final int TRY_COUNT = 10000;
        Random r = new Random();
        for (int i = 0; i < TRY_COUNT; i++) {
            int index = r.nextInt(COUNT);
            chs.get(index).fly();
            chs.get(index).shout();
        }

        for (CustomThread thread : threads) {
            thread.stopRun();
        }
    }

    public void fly() {
        //如果当前的线程是绑定的线程
        if (thread.isCurrentThread()) {
            internalFly();
        } else {
            thread.addTask(this::internalFly);
        }
    }

    public void shout() {
        //如果当前的线程是绑定的线程
        if (thread.isCurrentThread()) {
            internalShout();
        } else {
            thread.addTask(this::internalShout);
        }
    }

    private void internalFly() {
        System.out.println(Thread.currentThread().getName() + ":" + name + " is Flying!");
    }

    private void internalShout() {
        System.out.println(Thread.currentThread().getName() + ":" + name + " is Shouting!");
    }
}
