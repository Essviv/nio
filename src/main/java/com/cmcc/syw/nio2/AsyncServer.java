package com.cmcc.syw.nio2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunyiwei on 2016/1/27.
 */
public class AsyncServer {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        final int BLOCK = 2048;
        final int THREAD_SIZE = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService service = Executors.newFixedThreadPool(THREAD_SIZE);

        AsynchronousServerSocketChannel assc = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(InetAddress.getByName("localhost"), 8888));

        while (true) {
            //等待连接
            AsynchronousSocketChannel asc = assc.accept().get();

            //接收客户端的数据
            service.execute(() -> {
                try {
                    System.out.println("Receive connect from " + asc.getRemoteAddress());

                    ByteBuffer byteBuffer = ByteBuffer.allocate(BLOCK);

                    asc.read(byteBuffer, null, new CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                            byteBuffer.flip();

                            CharBuffer words = Charset.forName("utf-8").decode(byteBuffer);
                            System.out.println("Client says: " + words);

                            //echo back
                            try {
                                asc.write(ByteBuffer.wrap(("I receive your words: \"" + words + "\"").getBytes())).get();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            System.out.println("Listen to client returns error: " + exc);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
