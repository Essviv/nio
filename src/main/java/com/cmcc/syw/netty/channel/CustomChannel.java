package com.cmcc.syw.netty.channel;

/**
 * 模拟jetty中的channel
 * <p>
 * 它有个特点, 在创建的时候会被指定相应的线程对象,后续这个channel所有的操作都会在这个线程中执行
 * Created by sunyiwei on 2016/11/26.
 */
public class CustomChannel {
    //当前channel指定的线程对象
    private final Thread thread;

    public CustomChannel(Thread thread) {
        this.thread = thread;
    }

    public void fly(){
        //如果当前的线程是绑定的线程
       if(Thread.currentThread() == thread){
           internalFly();
       }else{

       }
    }

    public void shout(){

    }

    private void internalFly(){

    }
}
