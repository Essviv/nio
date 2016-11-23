package com.cmcc.syw.practise;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * 练习Bytebuf的API使用
 * <p>
 * Created by sunyiwei on 2016/11/23.
 */
public class BytebufPractise {
    public static void main(String[] args) {
        //sequential
        sequentialRW();

        //random
        randomRW();

        //index op
        indexOp();

        //view op
        viewOp();

        //others
        others();
    }

    private static void others() {
        ByteBuf byteBuf = init(26);

        //通过array获取底层的数组，并修改某个位置的值
        changeFromByteArr(byteBuf);

        //通过byteBuffer获取nio的byteBuffer对象，并修改某个位置的值
        changeFromByteBuffer(byteBuf);
    }

    private static void changeFromByteBuffer(ByteBuf byteBuf) {
        ByteBuffer byteBuffer = byteBuf.nioBuffer(0, byteBuf.capacity());

        byte newValue = '&';
        int index = 15;
        byteBuffer.put(index, newValue);

        //会同时改变这个bytebuf相应位置的值
        assert byteBuffer.get(index) == newValue;
        assert byteBuf.getByte(index) == newValue;
    }

    private static void changeFromByteArr(ByteBuf byteBuf) {
        byte[] array = byteBuf.array();

        int index = 5;
        array[index] = 'z'; //改变这个数组的某个值

        //会同时改变这个bytebuf相应位置的值
        assert array[index] == 'z';
        assert byteBuf.getByte(index) == 'z';
    }

    private static void viewOp() {
        ByteBuf bytebuf = init(26);

        //duplicate view
        int index = 5;
        ByteBuf duplicate = bytebuf.duplicate();
        duplicate.setByte(5, 'z');
        assert 'z' == bytebuf.getByte(5);

        //slice view, 拥有独立的指针，但共享同一份内容
        int subLength = 10;
        ByteBuf slice = bytebuf.slice(0, subLength);
        slice.clear();  //默认slice视图时，默认的readerIndex = 0, writerIndex = length, 即无法写入的状态
        for (int i = 0; i < subLength; i++) {
            slice.writeByte('&');
        }

        //bytebuf的内容也被修改了
        for (int i = 0; i < subLength; i++) {
            assert bytebuf.getByte(i) == '&';
            assert slice.getByte(i) == '&';
        }

        //指针是独立的
        assert bytebuf.readerIndex() == 0;
        assert bytebuf.readableBytes() == 0;
        assert slice.readableBytes() == subLength;
        assert slice.writableBytes() == 0;
    }

    private static void indexOp() {
        int length = 26;
        ByteBuf byteBuf = Unpooled.buffer(length);

        //顺序写入
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            byteBuf.writeByte((byte) (r.nextInt(26) + 'a'));
        }

        assert byteBuf.writableBytes() == 0;
        assert byteBuf.readerIndex() == 0;

        //clear前先mark一下
        byteBuf.markReaderIndex();
        byteBuf.markWriterIndex();

        //clear
        byteBuf.clear();
        assert byteBuf.writerIndex() == 0;
        assert byteBuf.readerIndex() == 0;

        //reset操作会将之前的指针恢复
        byteBuf.resetReaderIndex();
        byteBuf.resetWriterIndex();
        assert byteBuf.writableBytes() == 0;
        assert byteBuf.readerIndex() == 0;
    }

    private static void sequentialRW() {
        int length = 26;
        ByteBuf byteBuf = Unpooled.buffer(length);

        //顺序写入
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            byteBuf.writeByte((byte) (r.nextInt(26) + 'a'));
        }

        //当前已经无法再写入了
        assert byteBuf.writableBytes() == 0;
        assert byteBuf.readerIndex() == 0;

        //顺序读出
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) byteBuf.readByte());
        }

        //当前已经无法再读取了
        assert byteBuf.readableBytes() == 0;
        assert byteBuf.readerIndex() == byteBuf.writerIndex();
        System.out.println(sb);
    }

    private static void randomRW() {
        int length = 26;
        ByteBuf byteBuf = Unpooled.buffer(length);

        //随机写入
        for (int i = 0; i < length; i++) {
            byteBuf.setByte(i, (byte) (i + 'a'));
        }

        //不影响ByteBuf中指标的位置
        assert byteBuf.readableBytes() == 0;
        assert byteBuf.readerIndex() == 0;

        final int COUNT = 100;
        StringBuilder stringBuilder = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < COUNT; i++) {
            stringBuilder.append((char) byteBuf.getByte(r.nextInt(length)));
        }

        //不影响ByteBuf中指标的位置
        assert byteBuf.readableBytes() == 0;
        assert byteBuf.readerIndex() == 0;

        System.out.println(byteBuf.toString());
        System.out.println(stringBuilder);
    }

    private static ByteBuf init(int length) {
        ByteBuf byteBuf = Unpooled.buffer(length);

        //随机写入
        for (int i = 0; i < length; i++) {
            byteBuf.setByte(i, (byte) (i + 'a'));
        }

        return byteBuf;
    }
}
