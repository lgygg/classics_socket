package com.lgy.classics_socket;

import java.io.IOException;
import java.net.Socket;

/**
 * 通信者是客户端的时候，行为方式
 * @author: Administrator
 * @date: 2023/9/15
 */
public interface Client extends Communicator<CommunicatorInfo>{
    void connect() throws IOException;
}
