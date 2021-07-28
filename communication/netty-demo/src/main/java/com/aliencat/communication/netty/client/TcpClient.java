package com.aliencat.communication.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.aliencat.communication.netty.core.DefaultFuture;
import com.aliencat.communication.netty.handler.ClientHandler;
import com.aliencat.communication.netty.param.Request;
import com.aliencat.communication.netty.param.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TcpClient {
    public static EventLoopGroup group = null;
    public static Bootstrap boostrap = null;
    public static ChannelFuture future = null;

    static {
        group = new NioEventLoopGroup();
        boostrap = new Bootstrap();
        boostrap.channel(NioSocketChannel.class);
        boostrap.group(group);
        boostrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        boostrap.option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder
                                (Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ClientHandler());
                        ch.pipeline().addLast(new StringEncoder());


                    }
                });
        try {
            future = boostrap.connect("localhost", 8080).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//		boostrap.option(ChannelOption.RCVBUF_ALLOCATOR,AdaptiveRecvByteBufAllocator.DEFAULT);
    }

    public static Object send(Request request) {

        try {
            future.channel().writeAndFlush(JSONObject.toJSONString(request));
            future.channel().writeAndFlush("\r\n");
            DefaultFuture defaultFuture = new DefaultFuture(request);
            Response response = defaultFuture.get(10);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

}
