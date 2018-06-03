package com.wyl.study.demo2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {

    public static final String CHARCODE = "UTF-8";
    public static final String NEWLINE = "\r\n";
    public static final int port = 8821;

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 50,
                3000L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        List<Sender> senders = new ArrayList<>();

        for (int i = 0; i < 2000; i++) {
            senders.add(new Sender(i, port));
        }

        boolean flag = false;
        while (true) {
            senders.stream().forEach(sender -> threadPoolExecutor.submit(sender));
            if (flag) {
                break;
            }
            TimeUnit.SECONDS.sleep(3);
        }
    }

    public static class Sender implements Runnable {

        private int port;
        private int id;
        private String taskName;

        public Sender(int id, int port) {
            this.id = id;
            this.port = port;
            this.taskName = "Client_" + this.id;
        }

        @Override
        public void run() {
            Socket socket = null;

            OutputStream socketOut = null;
            BufferedReader bufferedReader = null;
            BufferedWriter bufferedWriter = null;
            try {
                long start = System.currentTimeMillis();
                socket = new Socket("127.0.0.1", port);
                socket.setSoTimeout(10);
                socket.setReuseAddress(true);
                socket.getTcpNoDelay();
                // 发送消息
                String msg = "From " + taskName;
                // base64 编码，防止中文乱码
                msg = Util.encode(msg.getBytes(CHARCODE));
                msg = msg + NEWLINE;
                bufferedWriter = Util.getBufferedWriter(socket);
                bufferedWriter.write(msg);
                bufferedWriter.flush();

                // 接收服务器的反馈
                bufferedReader = Util.getBufferedReader(socket);
                String res = bufferedReader.readLine();
                if (res != null) {
                    res = Util.decode(res, CHARCODE);
                    //System.out.println("Reply : " + res);
                }
                long end = System.currentTimeMillis();
                System.out.println(taskName + " 完成发送数据!" + " 耗时 : " + ((end - start)));
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println();
            } finally {
                try {
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (socketOut != null) {
                    try {
                        socketOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}