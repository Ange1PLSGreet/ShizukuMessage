package org.cookiebyte.dev.announce;

import java.io.IOException;

public interface ClientSocketInterface {

    /**
     * 运行客户端
     */
    public void RunClient();

    /**
     * 客户端连接
     */
    public void ConnectToServer();

    /**
     * 客户端向服务端发送消息
     */
    public void SendMessageToServer();



}
