package org.cookiebyte.dev.server;

import org.cookiebyte.dev.announce.ClientSocketInterface;
import org.cookiebyte.dev.announce.SocketInterface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.cookiebyte.dev.cryptor.ShizukuCryptorImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketInterfaceImpl implements ClientSocketInterface, SocketInterface, UnionLogInterface {

    public String ip = null;

    public int port = 0;

    public String message = "";

    public Socket socket = null;

    public PrintWriter output = null;

    public BufferedReader input = null;

    @Override
    public void RunClient() {
        try {
            ConnectToServer();
            SendMessageToServer();
        } finally {
            close(); // 确保资源最终关闭
        }
    }

    // Multi Thread Conn Impl
    public void ConnectToServer() {
        Thread connectThread = new Thread(() -> {
            try {
                this.socket = new Socket(this.ip, this.port);
                this.output = new PrintWriter(this.socket.getOutputStream(), true);
                this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            } catch (IOException e) {
                log.error("Connection error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
        connectThread.start();
        try {
            connectThread.join(); // 等待连接线程执行完毕
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for connection: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void SendMessageToServer() {
        Thread sendThread = new Thread(() -> {
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

                log.info("Crypted MSG:" + CryptorMessage);
                log.info("Message Sent By Client(Original):" + message);

                if (input != null) {
                    String response = input.readLine();
                    System.out.println("Received:" + response);
                } else {
                    log.error("Input stream is not initialized");
                }
            } catch (IOException e) {
                log.error("Error sending/receiving message: " + e.getMessage());
            }
        });
        sendThread.start();
        try {
            sendThread.join(); // 等待发送线程执行完毕
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for message sending: " + e.getMessage());
            Thread.currentThread().interrupt();
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
