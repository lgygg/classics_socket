package com.lgy.classics_socket;

import java.net.Socket;

/**
 * @author: Administrator
 * @date: 2023/9/19
 */
public interface ConnectedListener<T> {
    void onConnected(T t);
    void onClose(T t);
}
