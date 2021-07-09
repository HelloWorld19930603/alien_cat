package com.aliencat.communication.nio.server.example.client;

import java.io.*;
import java.net.Socket;

public class ClientIO {

    //服务端编码
    private static final String SERVERENCODEING = "UTF-8";

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                try {
                    doo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void doo() throws IOException {
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        String msg = "你好啊！";
        try {
            //发送数据
            socket = new Socket("127.0.0.1", 7777);
            out = new BufferedOutputStream(socket.getOutputStream());
            in = new BufferedInputStream(socket.getInputStream());
            out.write(msg.getBytes());
            out.flush();

            out.write("哈哈哈".getBytes());
            out.flush();
//            //任何的输入流或输出流的close()都会造成Socket关闭 但是不关闭又导致服务端无法接收到-1
//            out.close();
            socket.shutdownOutput();

            //读取正文内容
            byte[] flush = new byte[1024];
            int length = 0;
            StringBuffer rec = new StringBuffer();
            while ((length = in.read(flush)) != -1) {
                rec.append(new String(flush, 0, length, SERVERENCODEING));//以服务端编码标准发送
            }
            System.out.println("客户端收到回复：" + rec.toString());
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(socket, in, out);
        }
    }

    private static void close(Socket socket,
                              InputStream inputStream,
                              OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (socket != null) {
            socket.close();
        }
    }

}
