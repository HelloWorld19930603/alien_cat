package com.aliencat.javabase.api.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ScannerDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        System.out.println(line);
    }

    /**
     * 从控制台获取一行数据
     * * @throws IOException  readLine 可能会抛出异常
     */
    public static void getLine() throws IOException {

        // System.in是字节流，BufferedReader在构建的时候需要传入字符流
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 接收标准输入并转换为大写
        String str = br.readLine().toUpperCase();
        // 发送到标准输出
        System.out.println(str);
    }

    public void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(6789));
        serverSocketChannel.configureBlocking(false);
        while (true) {
            SocketChannel channel = serverSocketChannel.accept();
            register(channel);
            Set<SocketChannel> selectionKeys = selectedKeys();
            if (selectionKeys.size() != 0) {
                for (SocketChannel socketChannel : selectionKeys) {
                    handle(socketChannel);
                }
            }
        }
    }

    private void register(SocketChannel channel) {
        if (channel != null) {
            publicKeys.add(channel);
        }
    }

    private Set<SocketChannel> publicKeys = new HashSet<>();

    private Set<SocketChannel> selectedKeys() {
        Set<SocketChannel> publicSelectedKeys = new HashSet<>();
        for (SocketChannel fd : publicKeys) {
            if ((fd.validOps() & SelectionKey.OP_READ) != 0) {
                publicSelectedKeys.add(fd);
            }
        }
        return publicSelectedKeys;
    }

    private void handle(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        StringBuilder sb = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            sb.append(StandardCharsets.UTF_8.decode(byteBuffer));
        }
        if (sb.length() > 0) {
            System.out.println("服务端收到消息：" + sb.toString());
        }
    }


}
