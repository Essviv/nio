package com.cmcc.syw.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * random access file
 * <p>
 * Created by sunyiwei on 16/4/30.
 */
public class RandomFileAccess {
    public static void main(String[] args) throws IOException {
        final String FILENAME = "/tmp/path";
        final String CONTENT = "I'm here!";

        Path file = Paths.get(FILENAME);
        FileChannel fc =
                FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE);

        ByteBuffer out = ByteBuffer.wrap(CONTENT.getBytes());
        ByteBuffer copy = ByteBuffer.allocate(12);

        while(copy.hasRemaining()){
            fc.read(copy);
        }

        fc.position(0);
        while(out.hasRemaining()){
            fc.write(out);
        }

        long length = fc.size();
        fc.position(length - 1);

        copy.flip();
        while(copy.hasRemaining()){
            fc.write(copy);
        }

        out.flip();
        while(out.hasRemaining()){
            fc.write(out);
        }
    }
}
