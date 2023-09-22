package com.lgy.classics_socket;

/**
 * @author: Administrator
 * @date: 2023/9/21
 */
public class ChannelInfo {
    private CommunicatorInfo local;
    private CommunicatorInfo remote;

    public ChannelInfo() {
    }

    public ChannelInfo(CommunicatorInfo local, CommunicatorInfo remote) {
        this.local = local;
        this.remote = remote;
    }

    public CommunicatorInfo getLocal() {
        return local;
    }

    public void setLocal(CommunicatorInfo local) {
        this.local = local;
    }

    public CommunicatorInfo getRemote() {
        return remote;
    }

    public void setRemote(CommunicatorInfo remote) {
        this.remote = remote;
    }
}
