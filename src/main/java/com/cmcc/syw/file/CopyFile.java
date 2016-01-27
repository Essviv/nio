package com.cmcc.syw.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by sunyiwei on 16-1-22.
 */
public class CopyFile {
    public static void main(String[] args) throws IOException {
        final String FROM_FILE = "/tmp/from";
        final String TO_FILE = "/tmp/to";
        final int BLOCK = 1024 * 4;

        //create file stream
        FileInputStream fis = new FileInputStream(FROM_FILE);
        FileOutputStream fos = new FileOutputStream(TO_FILE);

        //retrieve file channels
        FileChannel inputFileChannel = fis.getChannel();
        FileChannel outputFileChannel = fos.getChannel();

        //allocate byte buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(BLOCK);

        while(-1 != inputFileChannel.read(byteBuffer)){
            byteBuffer.flip();
            outputFileChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        inputFileChannel.close();
        outputFileChannel.close();
        System.out.println("Copy is completed.");
    }
}
