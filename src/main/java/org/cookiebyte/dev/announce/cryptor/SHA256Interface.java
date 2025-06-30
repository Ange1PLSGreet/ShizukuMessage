package org.cookiebyte.dev.announce.cryptor;

public interface SHA256Interface {

    /**
     * SHA256 加密
     * @return String[]
     */
    public String[] Encrypt(String str);

    /**
     * 验证
     */
    public boolean Verify(String str, String salt, String hash);

    /**
     * 使用给定的盐值进行SHA256哈希计算
     * @param str 要加密的字符串
     * @param encodedSalt 已编码的盐值
     * @return 包含盐值和哈希值的数组
     */
    public String[] EncryptWithSalt(String str, String encodedSalt);
}
