package com.cmcc.syw.path;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by sunyiwei on 2016/1/28.
 */
public class PathTest {
    public static void main(String[] args) {
        final String srcFilename = "C:\\Users\\Lenovo\\Desktop\\test";
        final String tmpFilename = "C:\\Users\\Lenovo\\Desktop\\test_tmp";
        final String dstFilename = "C:\\Users\\Lenovo\\Desktop\\test_copy";

        //Construct paths
        Path srcPath = Paths.get(srcFilename);
        Path tmpPath = Paths.get(tmpFilename);
        Path dstPath = Paths.get(dstFilename);

        try {
            //recusively copy directory
            copy(srcPath, tmpPath);

            //move/rename
            if (Files.exists(dstPath)) {
                delete(dstPath);
            }
            Files.move(tmpPath, dstPath, StandardCopyOption.REPLACE_EXISTING);

            //dump files info
            dump(dstPath);

            //recusively delete
            delete(dstPath);

            System.out.println("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void delete(Path src) throws IOException {
        Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void dump(Path src) throws IOException {
        Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.format("%s: %s %n", file.getFileName(),
                        Files.probeContentType(file));
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void copy(Path srcDir, Path dstDir) throws IOException {
        if (Files.notExists(dstDir)) {
            Files.createDirectories(dstDir);
        }

        Files.walkFileTree(srcDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path dst = dstDir.resolve(srcDir.relativize(dir));
                Files.createDirectories(dst);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path dst = dstDir.resolve(srcDir.relativize(file));
                Files.copy(file, dst, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

                return FileVisitResult.CONTINUE;
            }
        });
    }
}
