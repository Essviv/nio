package com.cmcc.syw.nio;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * code to learn watch service in java
 * <p>
 * Created by sunyiwei on 16/5/1.
 */
public class DirWatchService {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String DIR = "/tmp";

        Path dir = Paths.get(DIR);

        WatchService watchService = FileSystems.getDefault().newWatchService();

        dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            WatchKey watchKey = watchService.take();

            List<WatchEvent<?>> watchEventList = watchKey.pollEvents();

            for (WatchEvent<?> watchEvent : watchEventList) {
                WatchEvent.Kind kind = watchEvent.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                WatchEvent<Path> event = (WatchEvent<Path>) watchEvent;
                Path filename = event.context();
                System.out.format("%s: %s. %n", filename, kind.toString());
            }

            boolean valid = watchKey.reset();
            if (!valid) {
                break;
            }
        }
    }
}
