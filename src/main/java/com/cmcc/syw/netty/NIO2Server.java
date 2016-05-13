package com.cmcc.syw.netty;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * example code to use NIO2
 * <p>
 * Created by sunyiwei on 16/5/9.
 */
public class NIO2Server {
    final static String HOST = "localhost";
    final static int PORT = 23432;

    public static void main(String[] args) throws IOException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        AsynchronousServerSocketChannel serverSocketChannel =
                AsynchronousServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, Object attachment) {
                serverSocketChannel.accept(null, this);

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                channel.read(byteBuffer, byteBuffer, new CustomCompletionHandler(channel));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    countDownLatch.countDown();
                }
            }
        });

        countDownLatch.await();
    }

}

class CustomCompletionHandler  implements CompletionHandler<Integer, ByteBuffer>{
    AsynchronousSocketChannel channel = null;

    public CustomCompletionHandler(AsynchronousSocketChannel asc) {
        this.channel = asc;
    }

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        byteBuffer.flip();

        channel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (attachment.hasRemaining()) {
                    channel.write(attachment);
                } else {
                    attachment.compact();
                    channel.read(attachment, attachment, this);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void failed(Throwable exc, ByteBuffer byteBuffer) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
