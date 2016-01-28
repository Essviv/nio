package com.cmcc.syw.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * Created by sunyiwei on 2016/1/27.
 */
public class FileAttributes {
    public static void main(String[] args) throws IOException {
        final String filename = "C:\\Users\\Lenovo\\Desktop\\gd.lnk";

        Path file = Paths.get(filename);
        BasicFileAttributeView bfav = Files.getFileAttributeView(file, BasicFileAttributeView.class);
        System.out.println(bfav.name());
        BasicFileAttributes bfas = bfav.readAttributes();

        System.out.println("IsDirectory: " + bfas.isDirectory());
        System.out.println("IsRegular: " + bfas.isRegularFile());
        System.out.println("IsSymbolicLink: " + bfas.isSymbolicLink());

        FileTime ft = Files.getLastModifiedTime(file);
        System.out.println(ft.toString());
    }
}
