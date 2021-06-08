package com.aliencat.communication.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * sayhello 客户端处理器
 */
public class SayHelloClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 通道信息读取处理
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg; // 将消息转化成bytebuf
        try {
            System.out.println("客户端直接打印接收到的消息: " + m.toString(Charset.defaultCharset()));
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            /**
             * 给服务端回复消息
             */
            ctx.writeAndFlush("客户端收到! 消息为: " + m.toString(Charset.defaultCharset()));
        } finally {
            m.release();
        }
    }

    /**
     * 连接成功后，自动执行该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 往服务端发送消息
         * 消息格式必须是ByteBuf才行!!!!!
         * 如果是其他格式服务端是接收不到的!!!!
         */
        String helo = "你好呀!";
        ByteBuf byteBuf = Unpooled.wrappedBuffer(helo.getBytes());
        ctx.channel().writeAndFlush(byteBuf);
        System.out.println("首次连接完成!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
