package com.aliencat.communication.netty;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class MyThread extends Thread {

    private Channel channel;
    private Object msg;

    @Override
    public void run() {
        channel.writeAndFlush(msg);
    }

}
