package com.lgy.classics_socket;

import android.text.TextUtils;

import com.lgy.classics_socket.util.CommonUtil;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Administrator
 * @date: 2023/9/21
 */
public class ChannelManager {

    private volatile static ChannelManager instance;
    private Map<String,Channel<ChannelInfo,Message>> channelMap;
    private List<ConnectedListener<ChannelInfo>> connectedListeners;
    private ChannelManager(){
        channelMap = new ConcurrentHashMap<>();
        connectedListeners = new CopyOnWriteArrayList();
    }

    public static ChannelManager getInstance(){
        if (instance == null) {
            synchronized (ChannelManager.class){
                if (instance == null) {
                    instance = new ChannelManager();
                }
            }
        }
        return instance;
    }

    private String getKeyBySocket(Socket socket){
        String localIp = socket.getLocalAddress().getHostAddress();
        String remoteIp = socket.getInetAddress().getHostAddress();
        int localPort = socket.getLocalPort();
        int remotePort = socket.getPort();
        if (!TextUtils.isEmpty(localIp) && !TextUtils.isEmpty(remoteIp)) {
            return CommonUtil.getKeyByMD5(localIp,localPort,remoteIp,remotePort);
        }
        return null;
    }

    private void notify(Channel<ChannelInfo,Message> channel){
        for (ConnectedListener<ChannelInfo> listener :
        connectedListeners) {
            listener.onConnected(channel.getChannelInfo());
        }
    }

    public void addSocket(Socket socket){
        if (socket.isConnected()) {
            String key = getKeyBySocket(socket);
            if (!channelMap.containsKey(key)) {
                Channel<ChannelInfo,Message> channel = new ClassicsChannel(socket);
                channelMap.put(key,channel);
                notify(channel);
            }
        }else {
            //TODO 未连接需要提供回调方法
        }
    }

    public void addChannel(Channel<ChannelInfo,Message> channel){
        if (channel.isConnected()) {
            CommunicatorInfo local = channel.getChannelInfo().getLocal();
            CommunicatorInfo remote = channel.getChannelInfo().getRemote();
            String key = CommonUtil.getKeyByMD5(local.getIp(),local.getPort(),remote.getIp(),remote.getPort());
            if (!channelMap.containsKey(key)) {
                channelMap.put(key,channel);
                notify(channel);
            }
        }else {
            //TODO 未连接需要提供回调方法
        }
    }

    public Channel<ChannelInfo,Message> getChannel(ChannelInfo info){
        if (info != null) {
            CommunicatorInfo local = info.getLocal();
            CommunicatorInfo remote = info.getRemote();
            String key = CommonUtil.getKeyByMD5(local.getIp(),local.getPort(),remote.getIp(),remote.getPort());
            return channelMap.get(key);
        }
        return null;
    }

    public Channel<ChannelInfo,Message> remoteChannel(CommunicatorInfo localInfo,CommunicatorInfo remoteInfo){
        return channelMap.remove(CommonUtil.getKeyByMD5(localInfo.getIp(),localInfo.getPort(),remoteInfo.getIp(),remoteInfo.getPort()));
    }

    public void addConnectedListener(ConnectedListener<ChannelInfo> connectedListener){
        this.connectedListeners.add(connectedListener);
    }

}
