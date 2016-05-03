package com.cmcc.syw.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

/**
 * Created by sunyiwei on 2016/5/3.
 */
public class ScatterAndGather {
    public static void main(String[] args) throws IOException {
        //gather
        gather();

        //scatter
        scatter();
    }

    private static void scatter() throws IOException {
        final int COUNT = 10;
        ByteBuffer[] byteBuffers = new ByteBuffer[COUNT];

        for (int i = 0; i < COUNT; i++) {
            byteBuffers[i] = ByteBuffer.allocate(2048);
        }

        final String FILENAME = "/tmp/path";
        Path path = Paths.get(FILENAME);
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
        fileChannel.read(byteBuffers);

        for (int i = 0; i < COUNT; i++) {
            printBuffer(byteBuffers[i]);
        }

        fileChannel.close();
    }

    private static void gather() throws IOException {
        final int COUNT = 10;

        //init byteBuffers
        ByteBuffer[] byteBuffers = new ByteBuffer[COUNT];
        for (int i = 0; i < COUNT; i++) {
            byteBuffers[i] = ByteBuffer.allocate(2048);

            randBuffer(byteBuffers[i]);
        }

        //gathering byteBuffers into file
        final String FILENAME = "/tmp/path";
        Path path = Paths.get(FILENAME);
        FileChannel fc = FileChannel.open(path, StandardOpenOption.WRITE);
        fc.write(byteBuffers);

        fc.close();
    }

    private static void printBuffer(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int capacity = byteBuffer.capacity();

        System.out.format("ByteBuffer: Position = %d, Limit = %d, Capacity = %d. %n", position, limit, capacity);
    }

    private static void randBuffer(ByteBuffer byteBuffer) {
        Random rand = new Random();

        while (byteBuffer.hasRemaining()) {
            byteBuffer.put((byte) (rand.nextInt(256)));
        }

        byteBuffer.flip();
    }
}
