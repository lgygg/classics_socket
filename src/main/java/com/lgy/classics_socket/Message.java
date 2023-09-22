package com.lgy.classics_socket;

import java.io.Serializable;

/**
 * @author: Administrator
 * @date: 2023/9/15
 */
public class Message implements Serializable {
    private String message;
    private CommunicatorInfo receiver;
    private CommunicatorInfo sender;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommunicatorInfo getReceiver() {
        return receiver;
    }

    public void setReceiver(CommunicatorInfo receiver) {
        this.receiver = receiver;
    }

    public CommunicatorInfo getSender() {
        return sender;
    }

    public void setSender(CommunicatorInfo sender) {
        this.sender = sender;
    }
}
