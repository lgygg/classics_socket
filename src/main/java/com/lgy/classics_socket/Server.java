package com.lgy.classics_socket;

/**
 * 定义通信者为服务器的行为
 * @author: Administrator
 * @date: 2023/9/15
 */
public interface Server<T> extends Communicator<CommunicatorInfo>{

    /**
     * 服务器准备好之后，等待客户端连接，处于监听状态
     */
    void listen();
    void close();
    boolean isClosed();
}
