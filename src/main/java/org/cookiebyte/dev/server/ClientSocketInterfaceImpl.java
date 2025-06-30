package org.cookiebyte.dev.server;

import org.cookiebyte.dev.announce.ClientSocketInterface;
import org.cookiebyte.dev.announce.SocketInterface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.cookiebyte.dev.cryptor.ShizukuCryptorImpl;
import org.cookiebyte.dev.thread.EventThreadInterfaceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketInterfaceImpl extends ClientSideAbstract implements ClientSocketInterface, SocketInterface, UnionLogInterface {

    public String ip = null;

    public int port = 0;

    public String message = "";

    public Socket socket = null;

    public PrintWriter output = null;

    public BufferedReader input = null;

    public ClientSocketInterfaceImpl(String service) {
        super(service);
    }

    @Override
    public void RunClient() {
        try {
            ConnectToServer();
            SendMessageToServer();
        } finally {
            close(); // 确保资源最终关闭
        }
    }

    @Override
    public void ConnectToServer() {
        ClientSideAbstract clientSideAbstract = new ClientSideAbstract("Connect") {
            @Override
            public void ConnectToServer() {
                super.ConnectToServer();
            }
        };
        EventThreadInterfaceImpl eventThreadInterface = new EventThreadInterfaceImpl();
        eventThreadInterface.event = new ClientSideAbstract("Connect") {
            @Override
            public void ConnectToServer() {
                super.ConnectToServer();
            }
        };
        eventThreadInterface.start();
    }

    @Override
    public void SendMessageToServer() {
        try {
            if (output == null) {
                log.error("Output stream is not initialized");
                return;
            }
            ShizukuCryptorImpl shizukuCryptor = new ShizukuCryptorImpl();
            String CryptorMessage = shizukuCryptor.EncodeMessageToHex(message);
            shizukuCryptor.StoreHexDataIntoArray();
            shizukuCryptor.InverseSortingArray();
            shizukuCryptor.HashEncryptArrayData();

           // output.println(message);
            log.info("Crypted MSG:" + CryptorMessage);
            log.info("Message Sent By Client:" + message);

            if (input != null) {
                String response = input.readLine();
                System.out.println("Received:" + response);
            } else {
                log.error("Input stream is not initialized");
            }
        } catch (IOException e) {
            log.error("Error sending/receiving message: " + e.getMessage());
        }
    }

    // 添加关闭方法
    @Override
    public void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            log.error("Error closing resources: " + e.getMessage());
        }
    }
}