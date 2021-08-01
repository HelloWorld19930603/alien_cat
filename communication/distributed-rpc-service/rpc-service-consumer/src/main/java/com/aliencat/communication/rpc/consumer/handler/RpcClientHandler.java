package com.aliencat.communication.rpc.consumer.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

/**
 * 客户端处理类
 * 1.发送消息
 * 2.接收消息
 */
@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable {

    ChannelHandlerContext context;
    //发送的消息
    String requestMsg;

    //服务端的消息
    String responseMsg;

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    /**
     * 通道连接就绪事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    /**
     * 通道读取就绪事件
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        responseMsg = msg;
        //唤醒等待的线程
        notify();
    }

    /**
     * 发送消息到服务端
     *
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        //消息发送
        context.writeAndFlush(requestMsg);
        //线程等待
        wait();
        return responseMsg;
    }
}
