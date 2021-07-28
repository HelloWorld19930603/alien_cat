package com.aliencat.communication.netty.param;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class Request {

    public static final AtomicLong nid = new AtomicLong(0);
    private final long id;
    private String command;
    private Object content;


    public Request() {
        id = nid.incrementAndGet();
    }

}
