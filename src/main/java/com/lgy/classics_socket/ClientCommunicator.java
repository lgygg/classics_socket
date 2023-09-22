package com.lgy.classics_socket;

import android.util.Log;

import com.google.gson.Gson;
import com.lgy.classics_socket.util.ThreadPoolUtil;

import java.io.IOException;

/**
 * @author: Administrator
 * @date: 2023/9/16
 */
public class ClientCommunicator {

    private Gson gson;
    private Client client;

    public ClientCommunicator(String ip,int port) {
        this.client = new ClassicsClient(ip,port);
        gson = new Gson();
    }

    public void starConnect(ConnectedListener<ChannelInfo> connectedListener){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ChannelManager.getInstance().addConnectedListener(connectedListener);
                    client.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                ChannelInfo info = new ChannelInfo(client.getInfo(),message.getReceiver());
                Log.e("qwe","==============1");
                Channel channel = ChannelManager.getInstance().getChannel(info);
                Log.e("qwe","==============2"+channel.isConnected());
                ((ClassicsChannel)channel).send(message);
                Log.e("qwe","==============3");
            }
        });
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
            ChannelManager.getInstance().remoteChannel(client.getInfo(),remote).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
