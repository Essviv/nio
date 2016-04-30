package com.cmcc.syw.nio;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * code to practise path operation
 * <p>
 * Created by sunyiwei on 16/4/27.
 */
public class PathOp {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println();
        create();

        System.out.println();
        read();

        System.out.println();
        update();

        System.out.println();
        delete();

        System.out.println();
        convert();

        System.out.println();
        compare();
    }

    private static void create() throws URISyntaxException, IOException {
        Path path = Paths.get("/tmp/path");
        Path uriPath = Paths.get("/tmp/test/1/2/3/path.ln");

        printPath(path);

        printPath(uriPath);
    }

    private static void read() {
        Path path = Paths.get("tmp/path");

        System.out.println(path.getRoot());
    }

    private static void update() {
        Path path = Paths.get("tmp/path");

        System.out.println(path.toUri());

        System.out.println(path.toAbsolutePath());

        try {
            System.out.println(path.toRealPath());
        } catch (IOException e) {
            System.err.println("Path is not exist.");
        }
    }

    private static void convert() {
        Path first = Paths.get("tmp/sub");
        Path second = Paths.get("hello/world");

        Path concat = first.resolve(second);
        System.out.println(concat);

        Path sibling = first.resolveSibling(second);
        System.out.println(sibling);

        Path relativize = first.relativize(second);
        System.out.println(relativize);
    }

    private static void compare() {
        Path sub = Paths.get("tmp/good");

        Path end = Paths.get("buddy");

        Path whole = Paths.get("tmp/good/hey/buddy/");

        System.out.println(whole.startsWith(sub));

        System.out.println(whole.endsWith(end));
    }

    private static void delete() {
        Path path = Paths.get("/tmp/././good/bad/../../path");

        Path normalized = path.normalize();

        System.out.println(normalized);
    }


    private static void printPath(Path path) throws IOException {
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
