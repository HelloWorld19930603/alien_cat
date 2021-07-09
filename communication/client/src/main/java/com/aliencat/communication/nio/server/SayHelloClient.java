package com.aliencat.communication.nio.server;

import com.aliencat.communication.nio.server.handler.SayHelloClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * sayhello 客户端
 */
public class SayHelloClient {

    private int port;
    private String host = "127.0.0.1";
    private Channel channel;

    public SayHelloClient(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建netty客户端
         */
        SayHelloClient client = new SayHelloClient(8686);
        /**
         * 新建一个线程让它单独去跑,我们可以main方法测试一下发送消息和接受消息
         */
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clientThread.start();
        /**
         * 如果不新建一个线程去跑客户端的话, 以下的代码就执行不到
         * 这里用while是因为客户端的channel并不能立马生成, 会在client启动后一段时间才生成获取到
         * 所以需要延迟一点获取channel, 否则channel为null
         */
        Channel channel = null;
        boolean isStart = false;
        while (!isStart) {
            if (null != client.getChannel()) {
                channel = client.getChannel();
                isStart = true;
            }
        }
        String helo = "你好呀!我这里是客户端, 收到请回答";
        ByteBuf byteBuf = Unpooled.wrappedBuffer(helo.getBytes());
        channel.writeAndFlush(byteBuf);
        /**
         * 我们通过控制台输入来给服务端发送消息
         * 此处只做模拟使用
         */
        for (int i = 0; i < 10; i++) {
            Scanner scanner = new Scanner(System.in);
            String text = scanner.nextLine();
            channel.writeAndFlush(Unpooled.wrappedBuffer(text.getBytes()));
        }
    }

    /**
     * 客户端运行方法
     *
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {
        /**
         * 负责装客户端的事件处理线程池
         */
        EventLoopGroup clientWorker = new NioEventLoopGroup();
        try {
            /**
             * netty客户端引导启动器
             */
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientWorker)//把事件处理线程池添加进启动引导器
                    .channel(NioSocketChannel.class)//设置通道的建立方式,这里采用Nio的通道方式来建立请求连接
                    //.option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            /**
                             * 此处添加客户端的通道处理器
                             */
                            socketChannel.pipeline().addLast(new SayHelloClientHandler());
                        }
                    });
            /**
             * 客户端绑定端口并且开始发起连接请求
             */
            ChannelFuture future = bootstrap.connect(host, port).sync();
            if (future.isSuccess()) {
                System.out.println("客户端连接服务器成功!");
            }
            /**
             * 将通道设置好, 以便外面获取
             */
            this.channel = future.channel();
            /**
             * 关闭客户端
             */
            future.channel().closeFuture().sync();
            System.out.println("客户端即将关闭!");
        } finally {
            /**
             * 关闭事件处理组
             */
            clientWorker.shutdownGracefully();
            System.out.println("客户端已关闭!");
        }
    }

    public Channel getChannel() {
        return this.channel;
    }
}