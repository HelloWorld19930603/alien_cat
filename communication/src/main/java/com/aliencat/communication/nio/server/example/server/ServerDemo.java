package com.aliencat.communication.nio.server.example.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ServerDemo {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("Server started with port number 8888...");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("有新客户端接入。。。");
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
                        inputStream = s.getInputStream();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            String line;
                            while ((line = reader.readLine()) != null && s.isConnected()) {
                                System.out.println("client:" + line);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("server 已退出通信！");
                    } finally {
                        if (s != null) {
                            try {
                                s.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }.setSocket(socket)).start();


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
                    OutputStream outputStream = null;
                    try {
                        outputStream = s.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try (BufferedOutputStream writer = new BufferedOutputStream(outputStream)) {
                        while (!(line = scanner.nextLine()).equals("exit") && s.isConnected()) {
                            line += "\n";
                            writer.write(line.getBytes(StandardCharsets.UTF_8));
                            writer.flush();
                        }
                        writer.write("\nGoodbye! client!".getBytes(StandardCharsets.UTF_8));
                        writer.flush();
                    } catch (IOException e) {
                        System.out.println("server 已退出通信！");
                    } finally {
                        if (s != null) {
                            try {
                                s.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }.setSocket(socket)).start();
        }
    }
}
