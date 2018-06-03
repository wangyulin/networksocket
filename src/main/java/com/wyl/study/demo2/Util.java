package com.wyl.study.demo2;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.Socket;

public class Util {

    public static String decode(String str, String charCode) {
        try {
            return new String(new BASE64Decoder().decodeBuffer(str), charCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode(byte[] bstr) {
        return new BASE64Encoder().encode(bstr);
    }

    public static PrintWriter getPrintWriter(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        return new PrintWriter(outputStream, true);
    }

    public static BufferedWriter getBufferedWriter(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        return new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public static BufferedReader getBufferedReader(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}