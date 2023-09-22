package com.lgy.classics_socket;

import java.net.Socket;

/**
 * 信道
 * @author: Administrator
 * @date: 2023/9/16
 */
public interface Channel<T,S> extends Communicate<S>{
    boolean isClose();
    boolean isConnected();
    T getChannelInfo();
}
