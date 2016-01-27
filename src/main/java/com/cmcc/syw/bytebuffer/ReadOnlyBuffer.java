package com.cmcc.syw.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * Created by sunyiwei on 16-1-23.
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        final int BLOCK = 32;
        ByteBuffer byteBuffer = ByteBuffer.allocate(BLOCK);

        Random r = new Random();
        for (int i = 0; i < BLOCK; i++) {
            byteBuffer.put((byte)('a' + r.nextInt(26)));
        }
        byteBuffer.flip();

        ByteBuffer readonlyByteBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println("Before: " + Charset.forName("utf-8").decode(readonlyByteBuffer));

        //do some modification
        for (int i = 0; i < BLOCK; i++) {
            byteBuffer.put((byte)('a' + i % 26));
        }

        //it proves that the modification in the original buffer will also affect the read-only one
        readonlyByteBuffer.flip();
        System.out.println("After: " + Charset.forName("utf-8").decode(readonlyByteBuffer));

    }
}
