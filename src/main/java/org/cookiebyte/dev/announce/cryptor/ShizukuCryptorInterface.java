package org.cookiebyte.dev.announce.cryptor;

public interface ShizukuCryptorInterface {

    /**
     * 将消息编码至16进制
     *
     * @return String
     */
    public String EncodeMessageToHex(String message);

    /**
     * 将编码至十六进制的消息存储到一个数组当中
     */
    public void StoreHexDataIntoArray();

    /**
     * 反排序数组
     */
    public void InverseSortingArray();

    /**
     * 哈希加密数组数据
     */
    public void HashEncryptArrayData();
}
