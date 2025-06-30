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

    private Socket socket = null;

    private PrintWriter output = null;

    private BufferedReader input = null;

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
        try {
            // 手动管理socket生命周期
            socket = new Socket(ip, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            log.error("Connection error: " + e.getMessage());
            throw new RuntimeException(e);
        }
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

            output.println(message);
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