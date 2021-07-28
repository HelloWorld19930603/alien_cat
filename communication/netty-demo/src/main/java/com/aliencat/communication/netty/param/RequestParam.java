package com.aliencat.communication.netty.param;

import lombok.Data;

@Data
public class RequestParam {

    private String command;
    private Object content;
    private long id;

}
