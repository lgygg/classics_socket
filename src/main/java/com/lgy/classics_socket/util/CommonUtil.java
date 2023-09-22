package com.lgy.classics_socket.util;

/**
 * @author: Administrator
 * @date: 2023/9/21
 */
public class CommonUtil {

    private CommonUtil(){}
    /**
     * 根据ip和端口获取md5值，作为connectedClient的key
     * @param localIp
     * @param localPort
     * @return
     */
    public static String getKeyByMD5(String localIp,Integer localPort,String remoteIp,Integer remotePort){
        if (localIp == null || localPort == null || remoteIp == null || remotePort == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(localIp).append(localPort)
                .append(remoteIp).append(remoteIp);
        return MD5Util.MD5(sb.toString());
    }
}
