package org.cookiebyte.dev.server;


import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.cookiebyte.dev.announce.ServerSocketInterface;
import org.cookiebyte.dev.announce.SocketInterface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;

public class ServerSocketInterfaceImpl implements ServerSocketInterface, SocketInterface, UnionLogInterface {

    protected ServerSocket serverSocket = null;

    protected PrintWriter out = null;

    protected Socket clientSocket = null;

    private BufferedReader in = null;

    @Override
    public void Initialize(int port) {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Initialize.. Listening on port:" + port);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public BufferedReader GetDataStream() {
        try {
            clientSocket = serverSocket.accept();
            log.info("GetDataStream. Socket accepted.");
            log.info("Client Connected. IP:" + clientSocket.getInetAddress());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return in;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public PrintWriter OutStream() {
        try {
            if (clientSocket == null || clientSocket.isClosed()) {
                log.error("Socket is not initialized or closed");
                return null;
            }
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            return out;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            log.error("Error closing resources: " + e.getMessage());
        }
    }
}