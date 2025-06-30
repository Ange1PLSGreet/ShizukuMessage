package org.cookiebyte.dev.cryptor;

import org.cookiebyte.dev.announce.cryptor.SHA256Interface;
import org.cookiebyte.dev.announce.log.UnionLogInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SHA256InterfaceImpl implements SHA256Interface, UnionLogInterface {

    /**
     * SHA256 加密，自动生成盐值
     * @param str String
     * @return String[] 包含盐值和哈希值的数组
     */
    @Override
    public String[] Encrypt(String str) {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            // 明确指定字符编码
            byte[] hashBytes = md.digest(str.getBytes("UTF-8"));
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String encodedHash = Base64.getEncoder().encodeToString(hashBytes);
            return new String[]{encodedSalt, encodedHash};
        } catch (Exception e) {
            log.error("加密时出错: " + e.getMessage());
            return null;
        }
    }

    /**
     * 使用给定的盐值进行SHA256哈希计算
     * @param str 要加密的字符串
     * @param encodedSalt 已编码的盐值
     * @return 包含盐值和哈希值的数组
     */
    public String[] EncryptWithSalt(String str, String encodedSalt) {
        try {
            byte[] salt = Base64.getDecoder().decode(encodedSalt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            // 明确指定字符编码
            byte[] hashBytes = md.digest(str.getBytes("UTF-8"));
            String encodedHash = Base64.getEncoder().encodeToString(hashBytes);
            return new String[]{encodedSalt, encodedHash};
        } catch (Exception e) {
            log.error("使用盐值加密时出错: " + e.getMessage());
            return null;
        }
    }

    /**
     * 验证字符串是否与给定的盐值和哈希值匹配
     *
     * @param str 待验证的字符串
     * @param salt 盐值
     * @param hash 哈希值
     */
    @Override
    public boolean Verify(String str, String salt, String hash) {
        try {
            byte[] decode_salt = Base64.getDecoder().decode(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(decode_salt);
            // 明确指定字符编码
            byte[] hashBytes = md.digest(str.getBytes("UTF-8"));
            String computedHash = Base64.getEncoder().encodeToString(hashBytes);
            return computedHash.equals(hash);
        } catch (Exception e) {
            log.error("验证时出错: " + e.getMessage());
            return false;
        }
    }
}
