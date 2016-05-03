package com.cmcc.syw.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Created by sunyiwei on 16-1-23.
 */
public class MemoryMappedBuffer {
    public static void main(String[] args) throws IOException {
        final String filename = "/tmp/test";
        RandomAccessFile raf = new RandomAccessFile(filename, "rw");

        long size = raf.length();
        FileChannel fileChannel = raf.getChannel();

        MappedByteBuffer mbb = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
        System.out.println("Before: " + Charset.forName("utf-8").decode(mbb));
        mbb.flip();

        for (int i = 0; i < size; i++) {
            mbb.put((byte)('a' + i % 26));
        }
        mbb.flip();
        System.out.println("After: " + Charset.forName("utf-8").decode(mbb));
        fileChannel.close();
    }
}
