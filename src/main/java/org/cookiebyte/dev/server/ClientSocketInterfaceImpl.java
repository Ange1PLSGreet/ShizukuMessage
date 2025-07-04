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

    public void StartIPCCommunication() {
        try (BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))) {
            ConnectToServer();
            String line;
            while ((line = stdin.readLine()) != null) {
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }
                message = line;
                SendMessageToServer();
                // 读取服务器响应并输出到标准输出
                String response = input.readLine();
                if (response != null) {
                    System.out.println(response);
                    System.out.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("IPC communication error: " + e.getMessage());
            log.error("IPC communication error: " + e.getMessage());
            System.exit(1);
        } finally {
            close();
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
                // 加密消息
                ShizukuCryptorImpl shizukuCryptor = new ShizukuCryptorImpl();
                shizukuCryptor.EncodeMessageToHex(message);
                shizukuCryptor.StoreHexDataIntoArray();
                shizukuCryptor.InverseSortingArray();
                shizukuCryptor.HashEncryptArrayData();
                String encryptedMessage = shizukuCryptor.ExportEncryptedData();

                // 发送加密后的消息
                output.println(encryptedMessage);
                output.flush(); // 刷新输出流，确保消息立即发送
                log.info("Crypted MSG:" + encryptedMessage);
                log.info("Message Sent By Client(Original):" + message);

                if (input != null) {
                    String response = input.readLine();
                    log.info("Received from server:" + response);
                    try {
                        // 将十六进制字符串转换为整数
                        int responseInt = Integer.parseInt(response, 16);
                        if (responseInt == 0x01){
                            log.info("Client has been connected to server");
                        } else{
                            log.error("Client has not been connected to server");
                        }
                    } catch (NumberFormatException e) {
                        log.error("Failed to parse server response as hexadecimal number: " + e.getMessage());
                    }
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
