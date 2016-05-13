package com.cmcc.syw.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * example code to use byteBuf
 * <p>
 * Created by sunyiwei on 16/5/10.
 */
public class ByteBufExample {
    static final String CONTENT = "hello world!";

    public static void main(String[] args) {
        //create
        create();

        //sequentialAccess
        sequentialAccess();

        //randomAccess
        randomAccess();

        //mark and reset
        markAndReset();

        //convert
        convert();

        //view
        view();

        //utils
        utils();
    }

    private static void create() {
        ByteBuf byteBuf = Unpooled.buffer();
        System.out.println("Init = " + byteBuf);

        final int LENGTH = byteBuf.capacity();
        for (int i = 0; i < LENGTH; i++) {
            byteBuf.writeByte(128);
        }

        System.out.println("After write = " + byteBuf);

        final String CONTENT = "hello world!";
        ByteBuf copyByteBuf = Unpooled.copiedBuffer(CONTENT.getBytes());
        System.out.println("After copied = " + copyByteBuf);
    }

    private static void sequentialAccess() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(CONTENT.getBytes());
        System.out.println("Init = " + byteBuf);

        StringBuilder sb = new StringBuilder();
        while (byteBuf.isReadable()) {
            sb.append(byteBuf.readByte());
        }
        System.out.println("After read = " + byteBuf);
    }

    private static void randomAccess() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(CONTENT.getBytes());
        System.out.println("Init = " + byteBuf);

        byteBuf.writerIndex(byteBuf.capacity() / 2);
        while (byteBuf.isWritable()) {
            byteBuf.writeByte(233);
        }

        System.out.println("After write = " + byteBuf);
    }


    private static void markAndReset() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(CONTENT.getBytes());
        System.out.println("Init = " + byteBuf);

        byteBuf.readerIndex(byteBuf.capacity() / 4);
        byteBuf.markReaderIndex();
        System.out.println("After mark = " + byteBuf);

        byteBuf.readerIndex(byteBuf.capacity() * 3 / 4);
        System.out.println("After change = " + byteBuf);

        byteBuf.resetReaderIndex();
        System.out.println("After reset = " + byteBuf);
    }

    private static void convert() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(CONTENT.getBytes());

        ByteBuffer byteBuffer = byteBuf.nioBuffer();

        byte[] bytes = byteBuf.array();
    }

    private static void view() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(CONTENT.getBytes());

        //share the same content
        ByteBuf duplicated = byteBuf.duplicate();

        ByteBuf slice = byteBuf.slice();

        //doesn't share the same content
        ByteBuf copied = byteBuf.copy();
    }

    private static void utils() {
        ByteBuf byteBuf = Unpooled.buffer(256);
        while (byteBuf.isWritable()) {
            byteBuf.writeByte(128);
        }

        ByteBuf secondByteBuf = ByteBufUtil.encodeString(new UnpooledByteBufAllocator(true),
                CharBuffer.wrap("hello world"),
                StandardCharsets.UTF_8);

        System.out.println(ByteBufUtil.prettyHexDump(secondByteBuf));
    }
}
