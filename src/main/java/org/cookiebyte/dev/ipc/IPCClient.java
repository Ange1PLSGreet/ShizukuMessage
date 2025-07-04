package org.cookiebyte.dev.ipc;

import org.cookiebyte.dev.server.ClientSocketInterfaceImpl;

/**
 * IPC客户端
 * 用于使用Java的客户端套接字实现
 * 这里的Main函数万万不可以动！
 */
public class IPCClient {

    public void IPCThreadStarter(String[] args){
        if (args.length < 2) {
            System.err.println("Usage: java IPCClientStarter <ip> <port>");
            System.exit(1);
        }
        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        ClientSocketInterfaceImpl client = new ClientSocketInterfaceImpl();
        client.ip = ip;
        client.port = port;
        client.StartIPCCommunication();
    }

    public static void main(String[] args) {
        IPCClient ipcClient = new IPCClient();
        ipcClient.IPCThreadStarter(args);
    }
}
