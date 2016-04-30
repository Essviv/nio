package com.cmcc.syw.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sunyiwei on 16/4/29.
 */
public class FileExample {

    public static void main(String[] args) throws IOException {
        //读取全部的文件内容
        readAllFile();

        //通过reader来读取文件内容
        System.out.println("=============");
        readByReader();


        //通过inputstream来读取文件内容
        System.out.println("=============");
        readByInputStream();

        //通过byteChannel来读取文件内容
        System.out.println("=============");
        readByByteChannel();
    }

    private static void readByByteChannel() throws IOException {
        Path path = tmpFile();

        SeekableByteChannel sbc = Files.newByteChannel(path);
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        String encoding = System.getProperty("file.encoding");
        while(sbc.read(byteBuffer)> 0){
            byteBuffer.flip();
            System.out.println(Charset.forName(encoding).decode(byteBuffer));

            byteBuffer.clear();
        }
    }

    private static void readByReader() throws IOException {
        Path path = tmpFile();

        BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(path)));

        String lineContent = null;
        while ((lineContent = br.readLine()) != null) {
            System.out.println(lineContent);
        }

        br.close();
    }

    private static void readByInputStream() throws IOException {
        Path path = tmpFile();

        InputStream is = Files.newInputStream(path);
        StringBuilder stringBuilder = new StringBuilder();

        int value = -1;
        while ((value = is.read()) != -1) {
            stringBuilder.append((char) value);
        }

        System.out.println(stringBuilder.toString());
    }


    private static void readAllFile() throws IOException {
        Path path = tmpFile();

        List<String> strings = Files.readAllLines(path);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    //产生随机文件
    private static Path tmpFile() {
        try {
            Path tmp = Files.createTempFile("tmp_", null);

            tmpContent(tmp);

            return tmp;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //随机产生文件内容
    private static void tmpContent(Path path) {
        try {
            final int COUNT = 10;

            List<String> strings = new ArrayList<String>(COUNT);
            for (int i = 0; i < COUNT; i++) {
                strings.add(randStr(10));
            }

            Files.write(path, strings, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String randStr(int length) {
        StringBuilder sb = new StringBuilder();

        Random r = new Random();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + r.nextInt(26)));
        }

        return sb.toString();
    }

}
