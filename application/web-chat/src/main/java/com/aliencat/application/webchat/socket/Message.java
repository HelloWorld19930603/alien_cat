package com.aliencat.application.webchat.socket;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message {
    private String id;
    private String msg;
    private String from;
    private String to;
    private String live;
}