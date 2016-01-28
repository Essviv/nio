package com.cmcc.syw.file;

import java.io.*;

/**
 * Created by sunyiwei on 16-1-27.
 */
public class DataInput {
    public static void main(String[] args) throws IOException {
        int i = 10;
        int j = 51;

        final String filename = "/tmp/test";
        DataOutputStream os =
                        new DataOutputStream(
                                new FileOutputStream(filename)
                        );
        os.writeInt(i);
        os.writeInt(j);
        os.close();

        DataInputStream dis =
                new DataInputStream(
                        new FileInputStream(filename)
                );
        System.out.println(dis.readInt());
        System.out.println(dis.readInt());
        dis.close();
    }
}
