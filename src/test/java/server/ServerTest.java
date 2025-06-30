package server;

import org.cookiebyte.dev.announce.ServerSocketInterface;
import org.cookiebyte.dev.server.ServerSocketInterfaceImpl;

import java.io.PrintWriter;

public class ServerTest {

    public static void main(String[] args) {
        ServerSocketInterface server = new ServerSocketInterfaceImpl();
        server.Initialize(8000);
        server.GetDataStream(); // 等待客户端连接
        PrintWriter out = server.OutStream();
        out.println("Server response"); // 发送响应
    }
}
