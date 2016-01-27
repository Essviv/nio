package com.cmcc.syw.sum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class Sum {
    private int sum(ByteBuffer bb) {
        int sum = 0;
        while (bb.hasRemaining()) {
            if ((sum & 1) != 0)
                sum = (sum >> 1) + 0x8000;
            else
                sum >>= 1;
            sum += bb.get() & 0xff;
            sum &= 0xffff;
        }

        return sum;
    }

    public void sum(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();

        long size = fc.size();
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);

        int sum = sum(mbb);
        int kb = (sum + 1023) / 1024;
        System.out.println(sum + "\t" + size + "\t" + f);
        fc.close();
    }

    public void sum(List<String> files){
        for (String file : files) {
            File f = new File(file);

            try {
                sum(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        List<String> files = new LinkedList<String>();
        files.add("/Users/sunyiwei/workspace/nio/nio.iml");
        files.add("/Users/sunyiwei/workspace/nio/pom.xml");
        files.add("/Users/sunyiwei/workspace/nio/src/main/java/com/cmcc/syw/sum/Sum.java");

        Sum sum = new Sum();
        sum.sum(files);
    }
}
