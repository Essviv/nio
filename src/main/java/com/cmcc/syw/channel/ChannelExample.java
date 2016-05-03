package com.cmcc.syw.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * example code to use channel
 * <p>
 * Created by sunyiwei on 16/5/2.
 */
public class ChannelExample {
    public static void main(String[] args) throws IOException {
        final String FILENAME = "/tmp/path";

        Path path = Paths.get(FILENAME);
        ByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ);

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        while (channel.read(byteBuffer) != -1) {
            byteBuffer.flip();

            while(byteBuffer.hasRemaining()){
                System.out.print((char)byteBuffer.get());
            }

            byteBuffer.clear();
        }

        channel.close();
    }
}
