package com.lgy.classics_socket;

import java.io.Serializable;

/**
 * 通信者信息
 * @author: Administrator
 * @date: 2023/9/15
 */
public class CommunicatorInfo implements Serializable {
    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
