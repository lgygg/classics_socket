package com.lgy.classics_socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author: Administrator
 * @date: 2023/9/16
 */
public class ClassicsChannel implements Channel<ChannelInfo,Message> {
    private Socket socket;
    private ObjectOutputStream oos;
    ObjectInputStream ois;

    public ClassicsChannel(Socket socket) {
        this.socket = socket;
    }


    public boolean isClose(Socket socket) {
        boolean result = false;
        try {
            socket.sendUrgentData(0xff);
        } catch (Exception e) {
            e.printStackTrace();
            result = true;
        }
        return result;
    }

    @Override
    public boolean isClose() {
        return isClose(socket);
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public ChannelInfo getChannelInfo() {
        return new ChannelInfo(getLocalCommunicatorInfo(), getRemoteCommunicatorInfo());
    }

    private CommunicatorInfo getLocalCommunicatorInfo() {
        CommunicatorInfo communicatorInfo = new CommunicatorInfo();
        communicatorInfo.setIp(socket.getLocalAddress().getHostAddress());
        communicatorInfo.setPort(socket.getLocalPort());
        return communicatorInfo;
    }

    private CommunicatorInfo getRemoteCommunicatorInfo() {
        CommunicatorInfo communicatorInfo = new CommunicatorInfo();
        communicatorInfo.setIp(socket.getInetAddress().getHostAddress());
        communicatorInfo.setPort(socket.getPort());
        return communicatorInfo;
    }

    /**
     * send message
     *
     * @param message the message will be sent
     * @return is successful. success: true; fail:false
     */
    @Override
    public void send(Message message) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * accept message
     */
    @Override
    public void accept(AcceptListener listener) {
        Message message = new Message();
        while (isConnected()) {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                message = (Message) ois.readObject();
                if (listener != null) {
                    listener.onAccept(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * close communication
     */
    @Override
    public void close() throws IOException {
        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket.close();
    }
}
