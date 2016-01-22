package com.cmcc.syw.executors;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class QueryExecutor implements Runnable {
    private String host;
    private int port;


    public QueryExecutor(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        int count = 0;
        while (count++ < 10) {
            try {
                SocketChannel sc = SocketChannel.open();
                InetSocketAddress isa = new InetSocketAddress(InetAddress.getByName(host), port);
                sc.connect(isa);

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                byteBuffer.clear();
                sc.read(byteBuffer);
                sc.close();

                byteBuffer.flip();

                CharBuffer cb = Charset.forName("utf-8").newDecoder().decode(byteBuffer);
                System.out.println(isa + ": " + cb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
