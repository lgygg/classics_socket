package com.lgy.classics_socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author: Administrator
 * @date: 2023/9/15
 */
public class ClassicsClient implements Client{

    private CommunicatorInfo remoteInfo;
    private Socket localSocket;
    public ClassicsClient(String remoteIp,int remotePort){
        remoteInfo = new CommunicatorInfo();
        remoteInfo.setPort(remotePort);
        remoteInfo.setIp(remoteIp);
        init();
    }

    private void init(){
        localSocket = new Socket();
    }


    @Override
    public void connect() throws IOException {
        InetSocketAddress address = new InetSocketAddress(remoteInfo.getIp(),remoteInfo.getPort());
        localSocket.connect(address);
        ChannelManager.getInstance().addSocket(localSocket);
    }

    @Override
    public CommunicatorInfo getInfo() {
        CommunicatorInfo communicatorInfo = new CommunicatorInfo();
        communicatorInfo.setIp(localSocket.getLocalAddress().getHostAddress());
        communicatorInfo.setPort(localSocket.getLocalPort());
        return communicatorInfo;
    }
}
