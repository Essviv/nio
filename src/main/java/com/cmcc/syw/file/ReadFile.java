package com.cmcc.syw.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Created by sunyiwei on 16-1-22.
 */
public class ReadFile {
    public static void main(String[] args) throws IOException {
        final String filename = "/tmp/test";
        final int BLOCK = 4096;

        //get file inputstream
        FileInputStream fis = new FileInputStream(filename);

        //get file channel
        FileChannel fc = fis.getChannel();

        //allocate buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(BLOCK);

        //read from channel to buffer
        fc.read(byteBuffer);
        byteBuffer.flip();

        System.out.println(Charset.forName("utf-8").newDecoder().decode(byteBuffer));
    }
}
