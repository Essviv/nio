package com.cmcc.syw.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by sunyiwei on 16-1-22.
 */
public class ReadFile {
    private final static String INPUT_FILENAME = "C:\\Users\\Lenovo\\Desktop\\test.txt";
    private final static String OUTPUT_FILENAME = "C:\\Users\\Lenovo\\Desktop\\test_out.txt";
    private final static int BLOCK = 4096;
    private final static Charset CHARSET = Charset.forName("gb2312");

    public static void main(String[] args) throws IOException {
//        channelRead();
//        commonlyReadAndWrite();
//        bufferedInputAndOutput();
        reverseContent();
    }

    private static void bufferedInputAndOutput() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILENAME), CHARSET));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_FILENAME), CHARSET))) {

            String content;
            while (StringUtils.isNotBlank(content = reader.readLine())) {
                System.out.println(content);

                //write to another file
                writer.write(content);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void commonlyReadAndWrite() throws IOException {
        Path file = Paths.get(INPUT_FILENAME);
        byte[] buffer = Files.readAllBytes(file);
        System.out.println(new String(buffer, Charset.forName("gb2312")));

        Files.write(Paths.get(OUTPUT_FILENAME), buffer, StandardOpenOption.CREATE);
    }

    private static void channelRead() throws IOException {
        //get file inputstream
        FileInputStream fis = new FileInputStream(INPUT_FILENAME);

        //get file channel
        FileChannel fc = fis.getChannel();

        //allocate buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(BLOCK);

        //read from channel to buffer
        fc.read(byteBuffer);
        byteBuffer.flip();

        System.out.println(Charset.forName("utf-8").newDecoder().decode(byteBuffer));
    }

    private static void reverseContent() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(INPUT_FILENAME, "r");
        FileChannel fc = raf.getChannel();

        StringBuffer stringBuffer = new StringBuffer();
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK);
        while (fc.read(buffer) > 0) {
            buffer.flip();
            stringBuffer.append(CHARSET.decode(buffer));
            buffer.clear();
        }

        System.out.println(stringBuffer);
        System.out.println(stringBuffer.reverse());
//        Files.write(Paths.get(OUTPUT_FILENAME), stringBuffer.toString().getBytes());
        FileUtils.writeStringToFile(new File(OUTPUT_FILENAME), stringBuffer.toString());
    }
}
