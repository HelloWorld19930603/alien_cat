package com.aliencat.communication;

import com.aliencat.communication.handler.SayHelloServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * sayhello 服务器
 */
public class SayHelloServer {

    /**
     * 端口
     */
    private int port;

    public SayHelloServer(int port) {
        this.port = port;
    }


    public static void main(String[] args) throws Exception {
        /**
         * 启动netty服务器
         */
        SayHelloServer sayHelloServer = new SayHelloServer(8686);
        sayHelloServer.run();
    }

    public void run() throws Exception {
        //Netty 负责装领导的事件处理线程池
        EventLoopGroup leader = new NioEventLoopGroup();
        // Netty 负责装码农的事件处理线程池
        EventLoopGroup coder = new NioEventLoopGroup();
        try {
            //服务端启动引导器
            ServerBootstrap server = new ServerBootstrap();

            server.group(leader, coder)//把事件处理线程池添加进启动引导器
                    .channel(NioServerSocketChannel.class)//设置通道的建立方式,这里采用Nio的通道方式来建立请求连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //构造一个由通道处理器构成的通道管道流水线
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //此处添加服务端的通道处理器
                            socketChannel.pipeline().addLast(new SayHelloServerHandler());
                        }
                    })
                    /**
                     * 用来配置一些channel的参数，配置的参数会被ChannelConfig使用
                     * BACKLOG用于构造服务端套接字ServerSocket对象，
                     * 标识当服务器请求处理线程全满时，
                     * 用于临时存放已完成三次握手的请求的队列的最大长度。
                     * 如果未设置或所设置的值小于1，Java将使用默认值50
                     */
                    .option(ChannelOption.SO_BACKLOG, 128)
                    /**
                     * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）
                     * 并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
                     */
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //服务端绑定端口并且开始接收进来的连接请求
            ChannelFuture channelFuture = server.bind(port).sync();
            //查看一下操作是不是成功结束了
            if (channelFuture.isSuccess()) {
                //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                System.out.println("服务端启动成功!");
            }
            // 关闭服务端
            channelFuture.channel().closeFuture().sync();
            System.out.println("服务端即将关闭!");
        } finally {
            //关闭事件处理组
            leader.shutdownGracefully();
            coder.shutdownGracefully();
            System.out.println("服务端已关闭!");
        }

    }
}
