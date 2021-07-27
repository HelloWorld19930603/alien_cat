package com.aliencat.communication.nio.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketIO {
    //客户端编码，客户端发送编码与服务端一致，则服务端无需进行解码特殊处理
    private static final String CLIENTENCODEING = "UTF-8";
    private static final int PORT = 7777;
    private static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("端口启动：" + PORT);
        while (true) {
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                //无数据时会阻塞在这里，有数据即accept事件会触发accept方法的调用
                //当一个连接进来，第二个连接进不来，因为单线程时，还阻塞在下面的read，线程无法再回到accept上继续等待
                socket = serverSocket.accept();
                int ccount = count.incrementAndGet();
                System.out.println("新的客户端已连接，当前No." + ccount + " " + System.currentTimeMillis());

                inputStream = new BufferedInputStream(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());

                //读取正文内容
                byte[] flush = new byte[1024];
                int length = 0;
                StringBuffer rec = new StringBuffer();
                while ((length = inputStream.read(flush)) != -1) {
                    rec.append(new String(flush, 0, length));
                }
                //写法2
                //客户端不通知关闭socket.shutdownOutput();时，用下面这种方法，不通知关闭read方法会死循环
                //available()方法可以在读写操作前先得知数据流里有多少个字节可以读取
                //但如果客户端分批发送可能有问题，可能无法获得第二批及以后的数据
                //所以最好还是让客户端通知一下
//                int count = 0;
//                while(count == 0){
//                    count = inputStream.available();
//                }
//                byte[] flush = new byte[count];
//                inputStream.read(flush);
//                String rec = new String(flush, 0, count, CLIENTENCODEING);

                String back = "[" + ccount + "]" + UUID.randomUUID() + "";
                System.out.println("收到数据：" + rec.toString() + " 即将返回数据：" + back);
                //返回数据
                outputStream.write(back.getBytes(), 0, back.getBytes().length);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(socket, inputStream, outputStream);
            }
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

