package com.aliencat.communication.nio.udp;

import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Slf4j
public class UDPServer {

    private static int port = 8010;

    public static void main(String[] args) throws Exception {
        // 创建接收端Socket对象
        DatagramSocket ds = new DatagramSocket(port);
        log.info("udp服务器启动成功...");
        // 接收数据
        byte[] bytes = new byte[1024];
        int length = bytes.length;
        DatagramPacket dp = new DatagramPacket(bytes, length);
        ds.receive(dp);
        // 解析数据
        InetAddress address = dp.getAddress();
        byte[] data = dp.getData();
        // 输出数据
        log.info(address.toString());
        log.info(new String(data, 0, data.length));
        ds.close();
    }
}
