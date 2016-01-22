package com.cmcc.syw.executors;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class ServerExecutor implements Runnable {
    private SocketChannel sc;

    public ServerExecutor(SocketChannel sc){
        this.sc = sc;
    }

    public void run() {
        System.out.println("收到请求,请求地址为:" + sc.socket().getRemoteSocketAddress());

        try {
            String now = Thread.currentThread().getName() + " returns "
                    + new SimpleDateFormat("HH:mm:ss").format(new Date());
            sc.write(Charset.forName("utf-8").newEncoder().encode(CharBuffer.wrap(now)));
            sc.close();
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
