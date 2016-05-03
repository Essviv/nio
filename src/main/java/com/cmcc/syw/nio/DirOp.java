package com.cmcc.syw.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * Created by sunyiwei on 16/4/30.
 */
public class DirOp {
    public static void main(String[] args) throws IOException {
        //list roots
        listRoots();

        //create dir
        createDir();

        //list dir
        listDir();

        //clear
        clear();
    }

    private static void listRoots() {
        Iterable<Path> paths = FileSystems.getDefault().getRootDirectories();

        for (Path path : paths) {
            System.out.println(path);
        }
    }

    private static void createDir() throws IOException {
        final String DIR = "/tmp/dir1/dir2";

        Set<PosixFilePermission> pfp = PosixFilePermissions.fromString("rwx------");
        FileAttribute<Set<PosixFilePermission>> fas =
                PosixFilePermissions.asFileAttribute(pfp);

        Files.createDirectories(Paths.get(DIR), fas);
        Files.createTempDirectory(Paths.get(DIR), "tmp-", fas);
    }

    private static void listDir() throws IOException {
        final String DIR = "/tmp/dir1/dir2";
        Files.createDirectories(Paths.get(DIR));

        DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get(DIR), "tmp-*");
        for (Path path : paths) {
            System.out.println(path);
        }
    }

    private static void clear() throws IOException {
        final String DIR = "/tmp/dir1";

        Path path = Paths.get(DIR);

        System.out.println("=============");

        Files.walkFileTree(path, new CustomFileVisitor());

        System.out.println("Clear is done!");
    }
}
