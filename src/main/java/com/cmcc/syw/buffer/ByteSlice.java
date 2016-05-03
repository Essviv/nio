package com.cmcc.syw.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * Created by sunyiwei on 16-1-22.
 */
public class ByteSlice {
    public static void main(String[] args) throws CharacterCodingException {
        final int BLOCK = 64;

        //wrap
        byte[] bytes = new byte[BLOCK];
        ByteBuffer wrapByteBuffer = ByteBuffer.wrap(bytes);

        //allocate
        ByteBuffer allocatedByteBuffer = ByteBuffer.allocate(BLOCK);

        //slice
        ByteBuffer parentByteBuffer = ByteBuffer.allocate(BLOCK);
        for (int i = 0; i < BLOCK; i++) {
            parentByteBuffer.put((byte) ('a' + i % 26));
        }
        parentByteBuffer.flip();
        System.out.println("Parent Before: "
                + Charset.forName("utf-8").newDecoder().decode(parentByteBuffer));

        //set position and limit
        parentByteBuffer.position(BLOCK / 4);
        parentByteBuffer.limit(BLOCK * 3 / 4);

        //slice and do some modification
        ByteBuffer subByteBuffer = parentByteBuffer.slice();
        System.out.println("Sub Before: "
                + Charset.forName("utf-8").newDecoder().decode(subByteBuffer));

        subByteBuffer.clear();
        int subSize = subByteBuffer.capacity();
        for (int i = 0; i < subSize; i++) {
            subByteBuffer.put((byte) (subByteBuffer.get(i) - 32));
        }
        subByteBuffer.flip();
        System.out.println("Sub After: "
                + Charset.forName("utf-8").newDecoder().decode(subByteBuffer));

        parentByteBuffer.clear();
        System.out.println("Parent After: "
                + Charset.forName("utf-8").newDecoder().decode(parentByteBuffer));
    }
}
