package com.aliencat.communication.netty.http;


import com.alibaba.fastjson.JSONObject;
import com.aliencat.communication.netty.param.Request;
import com.aliencat.communication.user.model.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;

public class NettyHttpClient {
    public static EventLoopGroup group = null;
    public static Bootstrap boostrap = null;

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
                        ch.pipeline().addLast(new HttpRequestEncoder());
                        ch.pipeline().addLast(new HttpResponseDecoder());
//							ch.pipeline().addLast(new HttpObjectAggregator(1048576));
                        ch.pipeline().addLast(new HttpClientHandler());
                    }
                });
//		boostrap.option(ChannelOption.RCVBUF_ALLOCATOR,AdaptiveRecvByteBufAllocator.DEFAULT);
    }

    public static void main(String[] args) {

        try {
            ChannelFuture future = boostrap.connect("localhost", 8080).sync();
            String person = "张三";

            String uri = "http://localhost:8080/";

            Request request = new Request();
            request.setCommand("saveUser");
            User user = new User();
            user.setAge("11");
            user.setId(1);
            user.setName("张三");
            request.setContent(user);

            ByteBuf content = Unpooled.wrappedBuffer(JSONObject.toJSONString(request).
                    getBytes(Charset.defaultCharset()));
            DefaultFullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri,
                    content);
//					req.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            req.headers().set(HttpHeaderNames.HOST, "localhost");
            req.headers().set(HttpHeaderNames.CONTENT_LENGTH, req.content().readableBytes());
            req.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);


            future.channel().writeAndFlush(req);

            future.channel().closeFuture().sync();

            Object result = future.channel().attr(AttributeKey.valueOf("ChannelKey")).get();
            System.out.println(result);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }


    }

}
