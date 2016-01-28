package com.cmcc.syw.path;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

/**
 * Created by sunyiwei on 2016/1/28.
 */
public class FileSystemDump {
    public static void main(String[] args) {
        FileSystem fileSystem = FileSystems.getDefault();
        Iterable<FileStore> fileStoreIterator = fileSystem.getFileStores();

        fileStoreIterator.forEach(fileStore -> {
            try {
                printFileStore(fileStore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void printFileStore(FileStore fs) throws IOException {
        final long K = 1024;
        final  long M = K * K;
        final long G = M * K;
        long total = fs.getTotalSpace() / G;
        long used = (fs.getTotalSpace() - fs.getUnallocatedSpace()) / G;
        long avail = fs.getUsableSpace() / G;

        System.out.format("%-20s %12d %12d %12d %n", fs.toString(), total, used, avail);
    }
}
