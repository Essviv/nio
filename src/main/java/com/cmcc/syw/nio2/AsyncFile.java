package com.cmcc.syw.nio2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by sunyiwei on 2016/1/27.
 */
public class AsyncFile {
    public static void main(String[] args) throws IOException {
        final String FILENAME = "C:\\Users\\Lenovo\\Desktop\\test.txt";
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(Paths.get(FILENAME),
                StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        ByteBuffer byteBuffer = ByteBuffer.wrap("Good bye!".getBytes());
        afc.write(byteBuffer, 0, "Write operation", new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                System.out.println(attachment + " completes with " + result + "bytes written.");
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("Write error: " + exc);
            }
        });
    }
}
