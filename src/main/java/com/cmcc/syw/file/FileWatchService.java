package com.cmcc.syw.file;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by sunyiwei on 2016/1/27.
 */
public class FileWatchService {
    public static void main(String[] args) throws IOException {
        final String filename = "C:\\Users\\Lenovo\\Desktop\\test";
        WatchService watchService = FileSystems.getDefault().newWatchService();

        Path path = Paths.get(filename);
        path.register(watchService, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        while(true) {
            try {
                WatchKey wk = watchService.take();
                for (WatchEvent<?> watchEvent : wk.pollEvents()) {
                    System.out.println(
                            "An event was found after file creation of kind " + watchEvent.kind()
                                    + ". The event occurred on file " + watchEvent.context() + ".");
                }
                wk.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
