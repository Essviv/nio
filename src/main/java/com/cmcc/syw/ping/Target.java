package com.cmcc.syw.ping;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class Target {
    private long begin = 0;
    private long end = 0;
    private Exception failure;
    private InetSocketAddress address;
    private SocketChannel socketChannel;

    public Target(String host, int port){
        try {
            address = new InetSocketAddress(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            failure = e;
        }
    }

    public void show() {
        String result;
        if (end != 0) {
            result = String.valueOf(end - begin);
        } else if (failure != null) {
            result = failure.toString();
        } else {
            result = "Unknown Error!";
        }

        System.out.println("Connect to " + address + "returns " + result);
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Exception getFailure() {
        return failure;
    }

    public void setFailure(Exception failure) {
        this.failure = failure;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
}
