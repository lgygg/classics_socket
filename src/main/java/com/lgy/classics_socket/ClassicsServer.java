package com.lgy.classics_socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: Administrator
 * @date: 2023/9/15
 */
public class ClassicsServer implements Server<Message> {

    //服务器信息
    private CommunicatorInfo info;
    private ServerSocket serverSocket;

    public ClassicsServer(int port){
        info = new CommunicatorInfo();
        info.setPort(port);
        init();
    }

    private void init(){
        try {
            serverSocket = new ServerSocket(info.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * close serverSocket
     */
    @Override
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClosed() {
        return serverSocket.isClosed();
    }

    /**
     * 服务器准备好之后，等待客户端连接，处于监听状态
     */
    @Override
    public void listen() {
        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                ChannelManager.getInstance().addSocket(socket);
                info.setIp(socket.getLocalAddress().getHostAddress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public CommunicatorInfo getInfo() {
        return info;
    }
}
