package client;

import org.cookiebyte.dev.announce.ClientSocketInterface;
import org.cookiebyte.dev.server.ClientSocketInterfaceImpl;

public class ClientTest {

    public static void main(String[] args) {
        ClientSocketInterfaceImpl client = new ClientSocketInterfaceImpl("Connect");
        client.ip = "localhost";
        client.port = 8000;
        client.message = "测试消息";
        client.RunClient();
    }
}
