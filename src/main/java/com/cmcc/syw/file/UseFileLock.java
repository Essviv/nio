package com.cmcc.syw.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunyiwei on 16-1-24.
 */
public class UseFileLock {
    public static void main(String[] args) throws IOException, InterruptedException {
        final int START = 10;
        final int SIZE = 20;
        final String filename = "/tmp/test";

        RandomAccessFile raf = new RandomAccessFile(filename, "rw");

        FileChannel fc = raf.getChannel();

        //get file lock
        FileLock fl = fc.lock(START, SIZE, false);

        System.out.println("Get file lock in " + System.currentTimeMillis());
        System.out.println("IsShared: " + fl.isShared());

        //pausing
        System.out.println("Pausing");
        Thread.sleep(3000);

        //release
        System.out.println("Release file lock in " + System.currentTimeMillis());
        fl.release();
        fc.close();
    }
}

