package org.cookiebyte.dev.ipc;

import java.io.IOException;
import java.net.Socket;

/**
 * 通过IPC总线传递服务器状态，使用布尔表示
 */
public class IPCBoolFlag {

    public static void main(String[] args){
        if (args.length < 2) {
            System.err.println("Usage: java IPCBoolFlag <ip> <port>");
            System.exit(1);
        }

        // Trying Ping Server
        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(ip, port), 3000);
            System.out.println("1");
        } catch (IOException e) {
            System.out.println("0");
        }
    }
}
