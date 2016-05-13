package com.cmcc.syw.netty;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * bioClient example
 *
 * Created by sunyiwei on 16/5/9.
 */
public class BIOClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        final int COUNT = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(COUNT);
        for (int i = 0; i < COUNT; i++) {
            executorService.submit(new CustomThread());
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            executorService.awaitTermination(1000, TimeUnit.MICROSECONDS);
        }
    }

    private static class CustomThread implements Runnable {
        @Override
        public void run() {
            try {
                final String HOST = "localhost";
                final int PORT = 22222;

                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(HOST, PORT));

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String[] contents = {
                        "hell, server",
                        "你好, 服务器",
                        "bye",
                        "end"
                };

                for (String content : contents) {
                    bufferedWriter.write(content);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }

                String resp = null;
                while (StringUtils.isNotBlank(resp = bufferedReader.readLine())) {
                    System.out.println(resp);
                }

                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
