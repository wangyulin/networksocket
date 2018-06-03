package com.wyl.study.base;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    public static ExecutorService executorService;
    public static final String NEWLINE = "\r\n";
    public static long COUNT = 0;

    public EchoServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", port));
        System.out.println("Starting echo server on port: " + port);
        while (true) {
            long start = System.currentTimeMillis();
            COUNT++;
            Socket socket = serverSocket.accept();
            ProcessTask processTask = new ProcessTask(socket, start);
            executorService.execute(processTask);

        }
    }

    public static void main(String[] args) throws IOException {
        executorService = Executors.newFixedThreadPool(5 * Runtime.getRuntime().availableProcessors());
        new EchoServer(9999);
    }

    public static class ProcessTask implements Runnable {

        private Socket socket;
        private long startTime;
        private BufferedReader br = null;
        private PrintWriter out = null;

        public ProcessTask(Socket socket, long startTime) {
            this.socket = socket;
            this.startTime = startTime;
            try {
                br = getReader(socket);
                out = getWriter(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                String msg;
                while ((msg = br.readLine()) != null) {

                    String res = "Server Reply : " + msg;
                    out.println(res);
                    out.flush();
                }
                long end = System.currentTimeMillis();
                System.out.println("Closing connection with client. 耗时 : " + ((end - startTime)));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                /*try {
                    //socket.shutdownInput();
                    //socket.shutdownOutput();

                    *//*if(null != br)
                        br.close();

                    if(null != out)
                        out.close();

                    if(null != socket)
                        socket.close();*//*

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }

        private PrintWriter getWriter(Socket socket) throws IOException {
            OutputStream socketOut = socket.getOutputStream();
            return new PrintWriter(socketOut, true);
        }

        private BufferedReader getReader(Socket socket) throws IOException {
            InputStream socketIn = socket.getInputStream();
            return new BufferedReader(new InputStreamReader(socketIn));
        }
    }
}
