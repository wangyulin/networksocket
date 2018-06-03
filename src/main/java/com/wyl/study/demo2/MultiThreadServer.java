package com.wyl.study.demo2;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.wyl.study.demo2.MultiThreadServer.CHARCODE;

public class MultiThreadServer {

    public static final String CHARCODE = "UTF-8";

    private final int POOL_SIZE = 10;
    private int port = 8821;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public MultiThreadServer() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", port));
        executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        System.out.println("Runtime.getRuntime().availableProcessors() = " + Runtime.getRuntime().availableProcessors());
        System.out.println("服务已启动");
    }

    public static void main(String[] args) throws IOException {
        new MultiThreadServer().service();
    }

    public void service() {
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                executorService.execute(new Handler(socket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

class Handler implements Runnable {

    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;

        try {

            long start = System.currentTimeMillis();

            bufferedReader = Util.getBufferedReader(socket);
            printWriter = Util.getPrintWriter(socket);

            String msg;
            while ((msg = bufferedReader.readLine()) != null) {
                msg = Util.decode(msg, CHARCODE);
                System.out.println("From : " + msg);

                String res = "服务器回复 : " + msg;
                res = Util.encode(res.getBytes(CHARCODE));

                printWriter.println(res);
                printWriter.flush();

                long end = System.currentTimeMillis();
                System.out.println("From : " + msg + " 耗时 : " + ((end - start)));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }
}