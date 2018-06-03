package com.wyl.study.base;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EchoClient {

    public static final int port = 9999;
    public static final String NEWLINE = "\r\n";
    public static final long NANOSECONDS_PER_SECOND = 1000 * 1000 * 1000;
    public static final long REQUESTS_PER_SECOND    = 1000 * 1000;

    public static long COUNT = 0;

    public static void main(String args[]) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                50, 50, 3000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < 5000; i++) {
            tasks.add(new Task(i, port));
        }

        boolean flag = false;
        while (true) {
            tasks.stream().forEach(
                    task ->
                    {
                        threadPoolExecutor.submit(task);
                        COUNT++;
                        try {
                            long sleep_time = NANOSECONDS_PER_SECOND / REQUESTS_PER_SECOND;
                            TimeUnit.NANOSECONDS.sleep(sleep_time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
            if (flag) {
                break;
            }
        }

        threadPoolExecutor.shutdown();
    }

    public static class Task implements Callable<Long> {

        private int port;
        private int id;
        private String taskName;

        public Task(int id, int port) {
            this.id = id;
            this.port = port;
            this.taskName = "Client_" + this.id;
        }

        public Long call() {
            long start = -1;
            long end = -1;
            try {
                Socket socket = new Socket("127.0.0.1", port);
                start = System.currentTimeMillis();
                String msg = "From " + taskName;
                msg = msg + NEWLINE;
                for (int i = 1; i <= 1; i++) {
                    OutputStream socketOut = null;
                    BufferedReader br = null;
                    try {
                        socketOut = socket.getOutputStream();
                        socketOut.write(msg.getBytes());
                        socketOut.flush();

                        br = new BufferedReader(new InputStreamReader(
                                socket.getInputStream()));
                        String res = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                    }
                }
                end = System.currentTimeMillis();

                System.out.println(taskName + " 完成发送数据!" + " 耗时 : " + ((end - start)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (end - start);
        }
    }
}
