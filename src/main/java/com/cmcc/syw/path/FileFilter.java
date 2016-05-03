package com.cmcc.syw.path;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by sunyiwei on 2016/1/29.
 */
public class FileFilter {
    public static void main(String[] args) throws IOException {
        final String filename = "C:\\Users\\Lenovo\\Desktop\\test";
        DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(filename), entry -> {
            return FilenameUtils.isExtension(entry.getFileName().toString(), "xlsx");
        });

        for (Path d : ds) {
            System.out.println(d.getFileName());
        }
    }
}
