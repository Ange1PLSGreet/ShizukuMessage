package org.cookiebyte.dev.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import org.cookiebyte.dev.announce.ServerSocketInterface;
import org.cookiebyte.dev.announce.SocketInterface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;
import org.cookiebyte.dev.cryptor.ShizukuCryptorImpl;

public class ServerSocketInterfaceImpl implements ServerSocketInterface, SocketInterface, UnionLogInterface {

    protected ServerSocket serverSocket = null;

    protected ExecutorService executorService = Executors.newCachedThreadPool();

    private BlockingQueue<BufferedReader> inputStreams = new LinkedBlockingQueue<>();

    private BlockingQueue<PrintWriter> outputStreams = new LinkedBlockingQueue<>();

    @Override
    public void Initialize(int port) {
        SetUpServer(port);
    }

    public void SetUpServer(int port) {
        Thread serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                log.info("Initialize.. Listening on port:" + port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    log.info("GetDataStream. Socket accepted.");
                    log.info("Client Connected. IP:" + clientSocket.getInetAddress());
                    executorService.submit(() -> HandleClient(clientSocket));
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
        serverThread.start();
    }

    private void HandleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            inputStreams.offer(in);
            outputStreams.offer(out);

            // 处理客户端输入
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                log.info("Received encrypted data from client: " + inputLine);
                // Decrypt: 解密接收到的加密消息
                ShizukuCryptorImpl decryptor = new ShizukuCryptorImpl();
                decryptor.ImportEncryptedData(inputLine);
                decryptor.VerifyAndDecryptCharacters();
                decryptor.ReverseArrayToOriginalOrder();
                decryptor.DecodeHexToOriginalMessage();
                String decryptedMessage = decryptor.GetDecryptedMessage();
                log.info("Decrypted message from client: " + decryptedMessage);
            }
        } catch (IOException e) {
            log.error("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                log.error("Error closing client socket: " + e.getMessage());
            }
        }
    }

    @Override
    public BufferedReader GetDataStream() {
        try {
            return inputStreams.poll(5, TimeUnit.SECONDS); // 等待 5 秒获取输入流
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public PrintWriter OutStream() {
        try {
            return outputStreams.poll(5, TimeUnit.SECONDS); // 等待 5 秒获取输出流
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void close() {
        executorService.shutdown();
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            log.error("Error closing server socket: " + e.getMessage());
        }
    }

}
