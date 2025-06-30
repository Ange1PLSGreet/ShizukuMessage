package org.cookiebyte.dev.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.cookiebyte.dev.announce.log.UnionLogInterface;

/**
 * Client Side Module
 */
public abstract class ClientSideAbstract implements UnionLogInterface {

    public void ConnectToServer(){
        try {
            // 手动管理socket生命周期
            ClientSocketInterfaceImpl csImpl = new ClientSocketInterfaceImpl("Connect");
            csImpl.socket = new Socket(csImpl.ip, csImpl.port);
            csImpl.output = new PrintWriter(csImpl.socket.getOutputStream(), true);
            csImpl.input = new BufferedReader(new InputStreamReader(csImpl.socket.getInputStream()));
        } catch (IOException e) {
            log.error("Connection error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ClientSideAbstract(String service){
        switch (service){
            case "Connect":
                ConnectToServer();
        }
    }
}
