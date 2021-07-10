package com.aliencat.communication.nio.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务端入站处理器适配器的继承类
 * 用来处理服务端的一些事情
 * 根据需要来实现一些方法
 */
public class SayHelloServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("直接打印服务端需要处理的信息: " + buf.toString(CharsetUtil.UTF_8));
        ByteBuf res = Unpooled.wrappedBuffer(new String("塔台收到!塔台收到!信息如下, 请确认 " + buf.toString(CharsetUtil.UTF_8)).getBytes());
        /**
         * 给客户端回复消息
         */
        ctx.writeAndFlush(res);
    }

    /**
     * 连接成功后，自动执行该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务器首次处理!");
        /**
         * 这种发送的消息格式是错误的!!!!!
         * 消息格式必须是ByteBuf才行!!!!!
         */
        ctx.writeAndFlush("Hello is server !");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        /**
         * 异常捕获
         */
        cause.printStackTrace();
        ctx.close();
    }
}