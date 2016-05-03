package com.cmcc.syw.buffer;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * example code to use byteBuffer
 *
 * Created by sunyiwei on 16/5/2.
 */
public class BufferExample {
    public static void main(String[] args) {
        final int COUNT = 256;

        Random rand  = new Random();

        ByteBuffer byteBuffer = ByteBuffer.allocate(COUNT);
        printBufferInfo(byteBuffer);

        for (int i = 0; i < COUNT / 2; i++) {
           byteBuffer.put((byte) rand.nextInt(256));
        }
        printBufferInfo(byteBuffer);

        byteBuffer.flip();
        printBufferInfo(byteBuffer);

        for (int i = 0; i < COUNT / 4; i++) {
            System.out.println((char)byteBuffer.get(i));
        }
        byteBuffer.mark();
        printBufferInfo(byteBuffer);

        for (int i = COUNT / 4; i < COUNT / 2; i++) {
            System.out.println((char)byteBuffer.get(i));
        }
        byteBuffer.reset();
        printBufferInfo(byteBuffer);

    }

    private static void printBufferInfo(ByteBuffer bb){
       int capacity = bb.capacity();
        int position = bb.position();
        int limit = bb.limit();

        System.out.format("Capicity = %s, Limit = %s, Position = %s. %n", capacity, limit, position);
    }
}
