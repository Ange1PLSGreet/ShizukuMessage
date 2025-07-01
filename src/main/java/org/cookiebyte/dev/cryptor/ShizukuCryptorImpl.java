package org.cookiebyte.dev.cryptor;

import org.cookiebyte.dev.announce.cryptor.ShizukuCryptorInterface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;

/**
 * Shizuku消息加密器/解密器
 * 支持将消息加密为特定格式，也支持从该格式还原原始消息
 */
public class ShizukuCryptorImpl implements ShizukuCryptorInterface, UnionLogInterface {

    protected static final HexTranslate hexTranslate = new HexTranslate();

    // 加密相关字段
    protected String originalMessage;
    protected String hexMessage;
    protected char[] dataArray;
    protected String[] encryptedHashes;
    protected String[] salts;

    // 解密相关字段
    public char[] decryptedCharArray;
    protected boolean isDecryptedData = false;

    /**
     * 将消息编码至16进制
     *
     * @param message
     * @return
     */
    @Override
    public String EncodeMessageToHex(String message) {
        this.originalMessage = message;
        this.hexMessage = hexTranslate.Str2HexStr(message);
        this.isDecryptedData = false;
        return this.hexMessage;
    }

    /**
     * 将编码至十六进制的消息存储到一个数组当中
     */
    @Override
    public void StoreHexDataIntoArray() {
        if (isDecryptedData) {
            dataArray = decryptedCharArray;
        } else {
            dataArray = hexMessage.toCharArray();
        }
    }

    /**
     * 反排序数组
     */
    @Override
    public void InverseSortingArray() {
        int left = 0;
        int right = dataArray.length - 1;
        while (left < right) {
            char temp = dataArray[left];
            dataArray[left] = dataArray[right];
            dataArray[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * 哈希加密数组数据
     */
    @Override
    public void HashEncryptArrayData() {
        SHA256InterfaceImpl sha256 = new SHA256InterfaceImpl();
        encryptedHashes = new String[dataArray.length];
        salts = new String[dataArray.length];

        for (int i = 0; i < dataArray.length; i++) {
            String charToEncrypt = String.valueOf(dataArray[i]);
            // 修正方法调用的大小写
            String[] result = sha256.Encrypt(charToEncrypt);

            if (result != null) {
                salts[i] = result[0];           // 保存盐值
                encryptedHashes[i] = result[1]; // 保存哈希值
            } else {
                salts[i] = null;
                encryptedHashes[i] = null;
            }
        }
    }

    public String ExportEncryptedData() {
        StringBuilder sb = new StringBuilder();
        sb.append(hexMessage.length()).append(":"); // 保存原始十六进制消息长度

        for (int i = 0; i < encryptedHashes.length; i++) {
            sb.append(salts[i]).append("|").append(encryptedHashes[i]).append(";");
        }
        return sb.toString();
    }

    public void ImportEncryptedData(String encryptedData) {
        String[] parts = encryptedData.split(":", 2);
        int hexLength = Integer.parseInt(parts[0]);
        String[] entries = parts[1].split(";");

        salts = new String[entries.length];
        encryptedHashes = new String[entries.length];

        for (int i = 0; i < entries.length; i++) {
            if (!entries[i].isEmpty()) {
                String[] saltHash = entries[i].split("\\|", 2);
                salts[i] = saltHash[0];
                encryptedHashes[i] = saltHash[1];
            }
        }

        this.isDecryptedData = true;
    }

    /**
     * 验证原始消息是否与加密数据匹配
     * @param originalMessage 待验证的原始消息
     * @return 如果匹配返回true，否则返回false
     */
    public boolean VerifyOriginalMessage(String originalMessage) {
        try {
            // 1. 将待验证的消息转换为十六进制并反转
            String hexMsg = hexTranslate.Str2HexStr(originalMessage);
            char[] chars = hexMsg.toCharArray();

            // 反转字符数组（与加密过程一致）
            int left = 0;
            int right = chars.length - 1;
            while (left < right) {
                char temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                left++;
                right--;
            }

            // 2. 使用原始加密时的盐值重新计算每个字符的哈希值
            SHA256InterfaceImpl sha256 = new SHA256InterfaceImpl();

            // 确保长度匹配
            if (chars.length != encryptedHashes.length) {
                return false;
            }

            // 逐个字符验证
            for (int i = 0; i < chars.length; i++) {
                String charToVerify = String.valueOf(chars[i]);
                String[] result = sha256.EncryptWithSalt(charToVerify, salts[i]);

                if (result == null || !result[1].equals(encryptedHashes[i])) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            log.error("验证消息时出错: " + e.getMessage());
            return false;
        }
    }

    /**
     * 验证并解密每个字符
     */
    public void VerifyAndDecryptCharacters() {
        SHA256InterfaceImpl sha256 = new SHA256InterfaceImpl();
        decryptedCharArray = new char[encryptedHashes.length];

        // 遍历所有可能的16进制字符
        char[] hexChars = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        for (int i = 0; i < encryptedHashes.length; i++) {
            String storedHash = encryptedHashes[i];
            String salt = salts[i];

            for (char c : hexChars) {
                // 修正方法调用的大小写
                String[] result = sha256.EncryptWithSalt(String.valueOf(c), salt);
                if (result != null && result[1].equals(storedHash)) {
                    decryptedCharArray[i] = c;
                    break;
                }
            }
        }
    }

    /**
     * 反转字符数组恢复原始顺序
     */
    public void ReverseArrayToOriginalOrder() {
        int left = 0;
        int right = decryptedCharArray.length - 1;
        while (left < right) {
            char temp = decryptedCharArray[left];
            decryptedCharArray[left] = decryptedCharArray[right];
            decryptedCharArray[right] = temp;
            left++;
            right--;
        }

        // 转换为十六进制字符串
        hexMessage = new String(decryptedCharArray);
    }

    /**
     * 将十六进制消息转回原始消息
     */
    public void DecodeHexToOriginalMessage() {
        originalMessage = hexTranslate.HexStr2Str(hexMessage);
    }

    /**
     * 获取解密后的原始消息
     * @return 原始消息字符串
     */
    public String GetDecryptedMessage() {
        return originalMessage;
    }

    public String[] GetSalts() {
        return salts;
    }

    public String[] GetEncryptedHashes() {
        return encryptedHashes;
    }
}
