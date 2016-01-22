package com.cmcc.syw.timeServer;

import com.cmcc.syw.executors.QueryExecutor;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunyiwei on 16-1-21.
 */
public class TimeQuery {
    public static void main(String[] args) throws IOException {
        final int port = 8888;
        final String host = "localhost";

        final int THREAD_COUNT = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(new QueryExecutor(host, port));
        }

        executorService.shutdown();
    }
}
