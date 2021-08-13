package com.aliencat.communication.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

public class NettyHttpServer {

    public static void main(String[] args) throws InterruptedException {
        //事件循环组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //启动对象
        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            //配置netty
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 请求解码器
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            // 将HTTP消息的多个部分合成一条完整的HTTP消息
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                            // 响应转码器
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            //添加处理器到pipeline
                            ch.pipeline().addLast("http-server", new HttpServerHandler());
                        }
                    });
            //启动服务,监听端口，同步返回
            ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress("127.0.0.1", 6666)).sync();
            // 当通道关闭时继续向后执行，这是一个阻塞方法
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

