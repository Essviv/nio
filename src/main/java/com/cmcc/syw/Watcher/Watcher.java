package com.cmcc.syw.Watcher;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

/**
 * Created by sunyiwei on 2016/1/22.
 */
public class Watcher {
    public static void main(String[] args) throws IOException {
        dump("C:\\Users\\Lenovo\\Desktop\\test.txt");
    }

    private static void watch(String filename) throws IOException, InterruptedException {
        Path path = Paths.get(filename);
        System.out.println("Watching to the current directory:" + path.toAbsolutePath());

        WatchService ws = path.getFileSystem().newWatchService();
        path.register(ws, StandardWatchEventKinds.ENTRY_CREATE);

        WatchKey wk = ws.take();
        List<WatchEvent<?>> events = wk.pollEvents();
        for (WatchEvent event : events) {
            System.out.println("Someone just created the file '" + event.context().toString() + "'.");
        }
    }

    private static void dump(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        FileChannel fc = fis.getChannel();

        long size = fc.size();
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);
        System.out.println(String.valueOf(Charset.forName("utf-8").newDecoder().decode(mbb)));
        fc.close();
    }
}
