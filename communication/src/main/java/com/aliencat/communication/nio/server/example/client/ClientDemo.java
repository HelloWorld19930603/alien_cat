package com.aliencat.communication.nio.server.example.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientDemo {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        System.out.println("Server connected successfully...");

        //处理输出流线程
        new Thread(new Runnable() {
            Socket s;

            Runnable setSocket(Socket s) {
                this.s = s;
                return this;
            }

            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                String line;
                OutputStream outputStream;
                try {
                    outputStream = s.getOutputStream();
                    try (BufferedOutputStream writer = new BufferedOutputStream(outputStream)) {
                        while (!(line = scanner.nextLine()).equals("exit") && s.isConnected()) {
                            line += "\n";
                            writer.write(line.getBytes(StandardCharsets.UTF_8));
                            writer.flush();
                        }
                        writer.write("Goodbye! server!".getBytes(StandardCharsets.UTF_8));
                        writer.flush();
                    } catch (Exception e) {
                        System.out.println("client 已退出通信！");
                    } finally {
                        if (s != null) {
                            try {
                                s.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("client 已退出通信！");
                }
            }
        }.setSocket(socket)).start();

        //处理输入流线程
        new Thread(new Runnable() {
            Socket s;

            Runnable setSocket(Socket s) {
                this.s = s;
                return this;
            }

            @Override
            public void run() {
                InputStream inputStream;
                try {
                    inputStream = socket.getInputStream();
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                        String line;
                        while ((line = bufferedReader.readLine()) != null && s.isConnected()) {
                            System.out.println("server:" + line);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("通信已经关闭了！");
                }
            }
        }.setSocket(socket)).start();
    }
}
