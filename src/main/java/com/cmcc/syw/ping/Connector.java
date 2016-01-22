package com.cmcc.syw.ping;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class Connector extends Thread {
    private Selector selector;
    private Printer printer;
    private LinkedList<Target> pending = new LinkedList<Target>();
    private boolean isRunning;

    public Connector(Printer printer) {
        isRunning = true;
        this.printer = printer;
        setName("Connector");

        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(Target target) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(false);

            target.setBegin(System.currentTimeMillis());
            target.setSocketChannel(sc);

            boolean connected = sc.connect(target.getAddress());
            if (connected) {
                target.setEnd(System.currentTimeMillis());
                sc.close();
                printer.add(target);
            } else {
                synchronized (pending) {
                    pending.add(target);
                }

                selector.wakeup();
            }
        } catch (IOException e) {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            target.setFailure(e);
            printer.add(target);
        }
    }

    public void shutdown() {
        isRunning = false;
        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                if (selector.select() > 0) {
                    processSelectedKeys();
                }

                processPendingTargets();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processPendingTargets(){
        synchronized (pending) {
            while(pending.size() > 0){
                Target target = pending.removeFirst();
                try {
                    target.getSocketChannel().register(selector, SelectionKey.OP_CONNECT, target);
                } catch (ClosedChannelException e) {
                    target.setFailure(e);

                    try {
                        target.getSocketChannel().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    printer.add(target);
                }
            }
        }
    }

    private void processSelectedKeys(){
        for(Iterator<SelectionKey> key = selector.selectedKeys().iterator(); key.hasNext();){
            SelectionKey selectionKey = key.next();
            key.remove();

            Target target = (Target)selectionKey.attachment();
            SocketChannel channel = (SocketChannel)selectionKey.channel();
            try {
                if(channel.finishConnect()){
                    target.setEnd(System.currentTimeMillis());
                    printer.add(target);
                    channel.close();
                    selectionKey.cancel();
                }
            } catch (IOException e) {
                target.setFailure(e);
                printer.add(target);

                try {
                    channel.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Printer printer = new Printer();
        Connector connector = new Connector(printer);

        printer.start();
        connector.start();

        String[] hosts = new String[]{"www.baidu.com", "t.sina.com.cn", "www.google.com"};
        int port = 80;

        for (String host : hosts) {
            Target target = new Target(host, port);
            connector.add(target);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        connector.shutdown();
        connector.join();

        for (Target target : printer.getTargets()) {
            target.show();
        }
    }
}
