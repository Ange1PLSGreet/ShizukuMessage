package server;

import org.cookiebyte.dev.announce.ServerSocketInterface;
import org.cookiebyte.dev.server.ClientSocketInterfaceImpl;
import org.cookiebyte.dev.server.ServerSocketInterfaceImpl;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTest {

    @Test
    public void testServerThenClient() throws InterruptedException {
        // 创建服务器实例
        ServerSocketInterface server = new ServerSocketInterfaceImpl();
        int port = 4900;
        // 启动服务器
        server.Initialize(port);

        // 使用线程池异步启动服务器接收数据
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            server.GetDataStream();
            PrintWriter out = server.OutStream();
            out.println(0x01);
        });

        // 创建客户端实例
        ClientSocketInterfaceImpl client = new ClientSocketInterfaceImpl();
        client.ip = "127.0.0.1";
        client.port = port;
        client.message = "测试消息";
        // 启动客户端
        client.RunClient();

        // 验证客户端连接成功
        assertNotNull(client.socket);

        // 关闭线程池
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
