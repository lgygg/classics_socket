package com.lgy.classics_socket;

import com.google.gson.Gson;
import com.lgy.classics_socket.util.ThreadPoolUtil;

import java.io.IOException;

/**
 * @author: Administrator
 * @date: 2023/9/16
 */
public class ServerCommunicator {

    private Gson gson;
    private Server server;

    public ServerCommunicator(int port) {
        this.server = new ClassicsServer(port);
        gson = new Gson();
    }



    public void startServer(ConnectedListener<ChannelInfo> connectedListener){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                ChannelManager.getInstance().addConnectedListener(connectedListener);
                server.listen();
            }
        });
    }

    /**
     * send message
     *
     * @param message the message will be sent
     * @return is successful. success: true; fail:false
     */
    public void send(Message message) {
        if (!server.isClosed()) {
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    ChannelInfo info = new ChannelInfo(message.getSender(),message.getReceiver());
                    ChannelManager.getInstance().getChannel(info).send(message);
                }
            });
        }
    }

    public void accept(ChannelInfo channelInfo,AcceptListener listener){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                ChannelManager.getInstance().getChannel(channelInfo).accept(listener);
            }
        });
    }

    public void close(CommunicatorInfo remote){
        try {
            ChannelManager.getInstance().remoteChannel((CommunicatorInfo) server.getInfo(),remote).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
