package com.cmcc.syw.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * example code to use byteBuffer
 * <p>
 * Created by sunyiwei on 16/5/2.
 */
public class BufferExample {
    public static void main(String[] args) {
        final int COUNT = 256;

        Random rand = new Random();

        ByteBuffer byteBuffer = ByteBuffer.allocate(COUNT);
        printBufferInfo(byteBuffer);

        for (int i = 0; i < COUNT / 2; i++) {
            byteBuffer.put((byte) rand.nextInt(256));
        }
        printBufferInfo(byteBuffer);

        byteBuffer.flip();
        printBufferInfo(byteBuffer);

        for (int i = 0; i < COUNT / 4; i++) {
            System.out.print(byteBuffer.get());
        }
        byteBuffer.mark();
        printBufferInfo(byteBuffer);

        for (int i = COUNT / 4; i < COUNT / 2; i++) {
            System.out.print(byteBuffer.get());
        }
        byteBuffer.reset();
        printBufferInfo(byteBuffer);

        //view
        viewOp();
    }

    private static void printBufferInfo(ByteBuffer bb) {
        int capacity = bb.capacity();
        int position = bb.position();
        int limit = bb.limit();

        System.out.format("Capicity = %s, Limit = %s, Position = %s. %n", capacity, limit, position);
    }

    private static void viewOp() {
        final int COUNT = 64;

        byte[] bytes = new byte[COUNT];
        for (int i = 0; i < COUNT; i++) {
            bytes[i] = 'a';
        }
        System.out.format("初始Byte数组的值为%s. %n", new String(bytes));

        System.out.println("修改ByteBuffer...");
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        for (int i = 0; i < COUNT / 2; i++) {
            byteBuffer.put((byte) 'b');
        }
        System.out.format("修改后的ByteBuffer的值为%s. %n", new String(byteBuffer.array(), StandardCharsets.UTF_8));
        System.out.format("修改后的Byte数组的值为%s. %n", new String(bytes));

        ByteBuffer slice = byteBuffer.slice();
        System.out.format("初始Slice的值为%s. %n", new String(slice.array(), StandardCharsets.UTF_8));

        System.out.println("修改bytes...");
        for (int i = 0; i < COUNT / 4; i++) {
            bytes[COUNT / 2 + i] = 'c';
        }

        System.out.format("修改后的ByteBuffer的值为%s. %n", new String(byteBuffer.array(), StandardCharsets.UTF_8));
        System.out.format("修改后的Byte数组的值为%s. %n", new String(bytes));
        System.out.format("修改后的Slice的值为%s. %n", new String(slice.array(), StandardCharsets.UTF_8));
    }
}
