package org.cookiebyte.dev.cryptor;

/**
 * 十六进制转换工具类
 * 提供字符串与十六进制字符串之间的转换功能
 */
public class HexTranslate {

    /**
     * 将字符串转换为十六进制字符串
     * @param str 原始字符串
     * @return 十六进制字符串
     */
    public static String Str2HexStr(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02X", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("字符串转十六进制失败", e);
        }
    }

    /**
     * 将十六进制字符串转换为原始字符串
     * @param hexStr 十六进制字符串
     * @return 原始字符串
     */
    public static String HexStr2Str(String hexStr) {
        try {
            int len = hexStr.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(hexStr.charAt(i), 16) << 4)
                        + Character.digit(hexStr.charAt(i+1), 16));
            }
            return new String(data, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("十六进制转字符串失败", e);
        }
    }
}
