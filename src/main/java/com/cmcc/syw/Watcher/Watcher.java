package com.cmcc.syw.Watcher;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by sunyiwei on 2016/1/22.
 */
public class Watcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String DIR_NAME = "C:\\Users\\Lenovo\\Desktop\\test";

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Paths.get(DIR_NAME).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
        while (true) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                System.out.println(watchEvent.context() + ": " + watchEvent.kind().name());
            }

            watchKey.reset();
        }
    }
}
