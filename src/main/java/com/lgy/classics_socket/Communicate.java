package com.lgy.classics_socket;

import java.io.IOException;

/**
 * 定义通信的行为
 * @author: Administrator
 * @date: 2023/9/15
 */
public interface Communicate<T> {

    /**
     * send message
     * @param t the message will be sent
     * @param <T>
     * @return is successful. success: true; fail:false
     */
    void send(T t);

    /**
     * accept message
     */
    void accept(AcceptListener listener);

    /**
     * close communication
     */
    void close() throws IOException;
}
