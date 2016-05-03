package com.cmcc.syw.nio;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;

/**
 * example code to practise file operations
 * <p>
 * Created by sunyiwei on 16/4/28.
 */
public class FileOp {
    private static final String EXIST_FILENAME = "/tmp/path";

    private static final String NO_EXIST_FILENAME = "/tmp/path/no_exist";

    private static final String LINK_FILENAME = "/tmp/test/1/2/3/path.ln";

    private static final String DIR_FILENAME = "/tmp";

    private static final String TO_CREATE = "/tmp/good";

    public static void main(String[] args) throws IOException {
        check();

        create(TO_CREATE);

        delete();

        copy();

        update();
    }

    private static void check() throws IOException {
        Path existPath = Paths.get(EXIST_FILENAME);

        Path noExistPath = Paths.get(NO_EXIST_FILENAME);

        Path dir = Paths.get(DIR_FILENAME);

        Path link = Paths.get(LINK_FILENAME);

        //check exist
        System.out.println(Files.exists(existPath));
        System.out.println(Files.notExists(noExistPath));
        System.out.println(Files.exists(dir));

        //check read/write/execute props
        printPath(existPath);

        printPath(noExistPath);

        printPath(dir);

        //check same file
        System.out.format("Same file? ==> %s. %n", Files.isSameFile(existPath, link));

    }


    private static void create(String filename) throws IOException {
        OutputStream os = new BufferedOutputStream(Files.newOutputStream(Paths.get(filename),
                StandardOpenOption.APPEND, StandardOpenOption.CREATE));

        final String content = "Good idea!";
        os.write(content.getBytes());
        os.close();

        System.out.println("Create file is done!");
    }

    private static void copy() throws IOException {
        Path src = Paths.get(EXIST_FILENAME);

        Path dst = Paths.get("/tmp/copy");

        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Copy file is done!");

        Path srcDir = Paths.get(DIR_FILENAME);

        Path dstDir = Paths.get("/tmp/copy_dir");

        Files.copy(srcDir, dstDir, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Copy dir is done.");
    }

    private static void delete() throws IOException {
        final String FILENAME = TO_CREATE;

        Path path = Paths.get(FILENAME);
        if (Files.notExists(path)) {
            create(FILENAME);
        }

        Files.delete(path);
        System.out.println("Delete file is done.");
    }

    private static void update() throws IOException {
        final String TMP_DIR = "/tmp/copy_dir";

        final String DST_DIR = "/tmp/dst_dir";

        Path src_path = Paths.get(DIR_FILENAME);

        Path tmp_path = Paths.get(TMP_DIR);

        Path dst_path = Paths.get(DST_DIR);

        Files.copy(src_path, tmp_path, StandardCopyOption.REPLACE_EXISTING);

        Files.move(tmp_path, dst_path);

        System.out.println("Update file is done.");
    }

    private static void find() {

    }

    private static void printPath(Path path) {
        final String PREFIX = path.toString();

        System.out.format("%s: %s. %n", PREFIX, Files.isReadable(path));
        System.out.format("%s: %s. %n", PREFIX, Files.isWritable(path));
        System.out.format("%s: %s. %n", PREFIX, Files.isExecutable(path));
        System.out.format("%s: %s. %n", PREFIX, Files.isRegularFile(path));
    }
}
