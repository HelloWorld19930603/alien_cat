package com.aliencat.communication.nio.udp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

@Slf4j
public class UDPSendTest {

    private String data = "床前明月光，疑似地上霜。 ..." + System.currentTimeMillis();
    private int port = 8010;
    private String host = "127.0.0.1";

    @Test
    public void test1() throws Exception {
        // 创建发送端Socket对象
        DatagramSocket ds = new DatagramSocket();
        // 创建数据并打包
        byte[] bytes = data.getBytes();
        InetAddress ip = InetAddress.getByName(host);// 根据自己主机的ip地址或者主机名
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, ip, port);
        // 发送数据
        ds.send(dp);
        log.info(data + " 发送成功！");
        // 释放资源
        ds.close();
    }


    @Test
    public void test2() throws IOException {
        //获得一个channel
        DatagramChannel channel = DatagramChannel.open();
        //发送消息

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(data.getBytes());
        buf.flip();
        channel.connect(new InetSocketAddress("127.0.0.1", 8010));
        channel.write(buf);
    }
}
