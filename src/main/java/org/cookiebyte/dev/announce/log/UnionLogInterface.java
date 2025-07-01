package org.cookiebyte.dev.announce.log;

// 修改导入的类
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface UnionLogInterface {
    // 使用 LogManager.getLogger 方法获取 Logger 实例
    public static final Logger log = LogManager.getLogger(UnionLogInterface.class);
}
