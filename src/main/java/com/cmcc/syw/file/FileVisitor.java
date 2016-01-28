package com.cmcc.syw.file;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by sunyiwei on 2016/1/27.
 */
public class FileVisitor {
    public static void main(String[] args) throws IOException {
        final String filename = "C:\\Users\\Lenovo\\Desktop\\test";
        Path file = Paths.get(filename);

        Files.walkFileTree(file, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("Entering into dir " + dir.getFileName());
                System.out.println(">>>>>>>>>>>>>>>>>>>");
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                System.out.println("Delete file " + file.getFileName());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                System.out.println("Delete dir " + dir.getFileName());
                System.out.println("Exist out of dir " + dir.getFileName());
                System.out.println("<<<<<<<<<<<<<<<<<<<<<");
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
