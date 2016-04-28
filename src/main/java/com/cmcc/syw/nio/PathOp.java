package com.cmcc.syw.nio;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * code to practise path operation
 *
 * Created by sunyiwei on 16/4/27.
 */
public class PathOp {
    public static void main(String[] args) throws IOException, URISyntaxException {
        create();
    }

    private static void create() throws URISyntaxException, IOException {
        Path path = Paths.get("/tmp/path");
        Path uriPath = Paths.get("/tmp/test/1/2/3/path.ln");

        printPath(path);

        printPath(uriPath);
    }

    private static void printPath(Path path) throws IOException {
        System.out.println();
        System.out.println("Root: " + path.getRoot());
        System.out.println(path.getFileName());
        System.out.println(path.getFileSystem());
        System.out.println(path.toString());
        System.out.println(path.getName(0));
        System.out.println("NameCount = " + path.getNameCount());
        System.out.println(path.getParent());
        System.out.println(path.toRealPath());
    }
}
