package com.aliencat.communication.rpc.provider.server;

import com.aliencat.communication.rpc.provider.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 启动类
 */
@Service
public class RpcServer implements DisposableBean {

    @Autowired
    RpcServerHandler rpcServerHandler;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    public void startServer(String ip, int port) {
        try {
            //1. 创建线程组
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            //2. 创建服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //3. 设置参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            //添加String的编解码器
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            //业务处理类
                            pipeline.addLast(rpcServerHandler);
                        }
                    });
            //4.绑定端口
            ChannelFuture sync = serverBootstrap.bind(ip, port).sync();
            System.out.println("==========服务端启动成功==========");
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }

            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
        }
    }


    @Override
    public void destroy() throws Exception {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }

        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }
}
