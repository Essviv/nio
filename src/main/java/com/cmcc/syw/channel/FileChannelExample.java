package com.cmcc.syw.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

/**
 * Created by sunyiwei on 2016/5/4.
 */
public class FileChannelExample {
    private static String FILENAME = "/tmp/path.fc";

    public static void main(String[] args) throws IOException {
        //example code to retrieve a fileChannel
        openFileChannel();

        //example code of fileChannel operations
        fileChannelOp();
    }

    private static ByteBuffer rand() {
        final int COUNT = 1024;
        Random random = new Random();
        ByteBuffer byteBuffer = ByteBuffer.allocate(COUNT);
        for (int i = 0; i < COUNT; i++) {
            byteBuffer.put((byte) random.nextInt(256));
        }

        byteBuffer.flip();
        return byteBuffer;
    }

    private static void fileChannelOp() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(FILENAME, "rw");
        FileChannel fc = raf.getChannel();

        //truncate
        fc.truncate(fc.size());

        //write
        fc.write(rand());

        //flush
        fc.force(true);

        //reposition
        final int OFFSET = 1024;
        long position = fc.position();
        fc.position(position + OFFSET);

        //write
        fc.write(rand());

        //read
        fc.position(0);
        long size = fc.size();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) size);
        fc.read(byteBuffer);

        byteBuffer.flip();
        System.out.println(new String(byteBuffer.array(), StandardCharsets.US_ASCII.name()));

        //close
        fc.close();
    }

    private static void openFileChannel() throws IOException {
        Path path = Paths.get(FILENAME);

        FileChannel fc = FileChannel.open(path, StandardOpenOption.READ);
        fc.close();

        FileChannel fc2 = new FileInputStream(path.toFile()).getChannel();
        fc2.close();

        FileChannel fc3 = new FileOutputStream(path.toFile()).getChannel();
        fc3.close();

        FileChannel fc4 = new RandomAccessFile(path.toFile(), "rw").getChannel();
        fc4.close();
    }
}
