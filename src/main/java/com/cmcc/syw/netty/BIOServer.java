package com.cmcc.syw.netty;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * bioServer example
 *
 * Created by sunyiwei on 16/5/9.
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        final int PORT = 22222;
        ServerSocket serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket socket = serverSocket.accept();

            System.out.format("接收到来自客户端的请求, Port = %s. %n", socket.getPort());

            //request per thread
            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    while (true) {
                        String content = reader.readLine();
                        if (content.equals("end")) {
                            writer.write("");

                            reader.close();
                            writer.close();
                            break;
                        }

                        writer.write(content);
                        writer.newLine();
                        writer.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
