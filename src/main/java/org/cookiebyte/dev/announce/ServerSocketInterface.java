package org.cookiebyte.dev.announce;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * 使用Socket实现基本通讯
 */
public interface ServerSocketInterface {

    /**
     * 实例化，初始化服务器
     *
     * @param port int
     */
    public void Initialize(int port);

    /**
     * 获取数据流
     * 此方法为收入流
     * 收入流：接受客户端发送的数据
     */
    public BufferedReader GetDataStream();

    /**
     * 获取数据流
     * 此方法为输出流
     * 输出流：向客户端发送数据
     */
    public PrintWriter OutStream();
}
