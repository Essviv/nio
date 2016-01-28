package com.cmcc.syw.nio2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by sunyiwei on 2016/1/27.
 */
public class AsyncClient {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel asc = AsynchronousSocketChannel.open();
        asc.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 8888)).get();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": Client has connect to server");
        asc.write(ByteBuffer.wrap("hello handsome guy.".getBytes())).get();
        System.out.println("Say hello to server.");

        final int BLOCK = 2048;
        ByteBuffer byteBuffer = ByteBuffer.allocate(BLOCK);
        asc.read(byteBuffer).get();
        byteBuffer.flip();
        System.out.println("Server returns: " + Charset.forName("utf-8").decode(byteBuffer));
        asc.close();
    }
}
