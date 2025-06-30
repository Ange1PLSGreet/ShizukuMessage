package org.cookiebyte.dev.announce.encryptor;

public interface ShizukuDecryptInterface {

    /**
     * 重新按顺序排列数组
     */
    public void PositiveSortingArray();

    /**
     * 使用哈希秘钥获取原数组数据（排序后）
     */
    public int[] GetDataByHashKey();

    /**
     * 重新将十六进制编码回原来的消息
     */
    public void EncodeToHex();
}
