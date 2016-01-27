package com.cmcc.syw.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * Created by sunyiwei on 16-1-22.
 */
public class WriteFile {
    public static void main(String[] args) throws IOException {
        final String filename = "/tmp/out";
        final int BLOCK = 4096 * 10;

        //get file output stream
        FileOutputStream fos = new FileOutputStream(filename);

        //get file channel
        FileChannel fc = fos.getChannel();

        //write to byte buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(BLOCK);
        Random r = new Random();
        for (int i = 0; i < BLOCK; i++) {
            byteBuffer.put((byte)('a' + r.nextInt(26)));
        }
        byteBuffer.flip();

        //write byte buffer to channel
        fc.write(byteBuffer);
        fc.close();
        System.out.println("Everything is ok.");
    }
}
