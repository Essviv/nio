package com.cmcc.syw.buffer;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by sunyiwei on 2016/5/3.
 */
public class ChannelToChannel {
    public static void main(String[] args) throws IOException {
        final String FROM = "/tmp/path.from";
        final String TO = "/tmp/path.to";

        //get fromChannel
        Path fromPath = Paths.get(FROM);
        FileChannel fromChannel = FileChannel.open(fromPath, StandardOpenOption.READ);

        //get toChannel
        Path toPath = Paths.get(TO);
        FileChannel toChannel = FileChannel.open(toPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        //transfer
        fromChannel.transferTo(0, fromChannel.size(), toChannel);

        //close
        fromChannel.close();
        toChannel.close();
    }
}
