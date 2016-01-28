package com.cmcc.syw.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by sunyiwei on 2016/1/28.
 */
public class FilesTest {
    public static void main(String[] args) {
        final String filename = "C:\\Users\\Lenovo\\Desktop\\test";
        Path path = Paths.get(filename);

        System.out.println("Exist: " + Files.exists(path));
        System.out.println("Readable: " + Files.isReadable(path));
        System.out.println("Writable: " + Files.isWritable(path));
        System.out.println("Executable: " + Files.isExecutable(path));
    }
}
