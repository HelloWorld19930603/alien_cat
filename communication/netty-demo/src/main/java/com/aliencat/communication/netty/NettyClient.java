package com.aliencat.communication.netty;


import com.aliencat.communication.netty.handler.ClientHandler;
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
import io.netty.util.AttributeKey;

public class NettyClient {
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
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder
                                (Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ClientHandler());
                        ch.pipeline().addLast(new StringEncoder());


                    }
                });
//		boostrap.option(ChannelOption.RCVBUF_ALLOCATOR,AdaptiveRecvByteBufAllocator.DEFAULT);
    }

    public static void main(String[] args) {

        try {
            ChannelFuture future = boostrap.connect("localhost", 8080).sync();
            String person = "张三";

//			ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
//			buf.writeBytes(person.getBytes(Charset.defaultCharset()));
            future.channel().writeAndFlush(person);
//			Thread.sleep(50000);
            future.channel().writeAndFlush("\r\n");
//			future.channel().writeAndFlush(Delimiters.lineDelimiter()[0]);

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
