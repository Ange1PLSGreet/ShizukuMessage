package cryptor;

import static org.junit.jupiter.api.Assertions.*;

import org.cookiebyte.dev.cryptor.HexTranslate;
import org.cookiebyte.dev.cryptor.SHA256InterfaceImpl;
import org.cookiebyte.dev.cryptor.ShizukuCryptorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CryptorTest {

    private ShizukuCryptorImpl cryptor;
    private SHA256InterfaceImpl sha256;

    @BeforeEach
    void setUp() {
        cryptor = new ShizukuCryptorImpl();
        sha256 = new SHA256InterfaceImpl();
    }

    @Test
    void testEncryptAndDecryptMessage() {
        // 原始消息
        String originalMessage = "Hello, this is a test message!";

        // 加密流程
        cryptor.EncodeMessageToHex(originalMessage);
        cryptor.StoreHexDataIntoArray();
        cryptor.InverseSortingArray();
        cryptor.HashEncryptArrayData();
        String encryptedData = cryptor.ExportEncryptedData();

        // 验证加密数据不为空
        assertNotNull(encryptedData);
        assertTrue(encryptedData.contains(":"));
        assertTrue(encryptedData.contains("|"));
        assertTrue(encryptedData.contains(";"));

        // 创建新的实例进行解密
        ShizukuCryptorImpl decryptor = new ShizukuCryptorImpl();

        // 解密流程
        decryptor.ImportEncryptedData(encryptedData);
        decryptor.VerifyAndDecryptCharacters();
        decryptor.ReverseArrayToOriginalOrder();
        decryptor.DecodeHexToOriginalMessage();
        String decryptedMessage = decryptor.GetDecryptedMessage();

        // 验证解密消息与原始消息匹配
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    void testEncryptWithSaltConsistency() {
        String testChar = "A";
        String[] result = sha256.Encrypt(testChar);
        String salt = result[0];
        String hash = result[1];

        // 使用相同的盐值进行加密
        String[] resultWithSalt = sha256.EncryptWithSalt(testChar, salt);
        String hashWithSalt = resultWithSalt[1];

        // 验证两次哈希结果相同
        assertEquals(hash, hashWithSalt);

        // 验证verify方法也能正确验证
        assertTrue(sha256.Verify(testChar, salt, hash));
    }

    @Test
    void testVerifyOriginalMessage() {
        String originalMessage = "Test verification";

        // 执行加密
        cryptor.EncodeMessageToHex(originalMessage);
        cryptor.StoreHexDataIntoArray();
        cryptor.InverseSortingArray();
        cryptor.HashEncryptArrayData();

        // 验证原始消息
        assertTrue(cryptor.VerifyOriginalMessage(originalMessage));

        // 验证错误消息
        assertFalse(cryptor.VerifyOriginalMessage("Wrong message"));
    }

    @Test
    void testHexConversion() {
        String originalMessage = "123abc";
        String hexMessage = HexTranslate.Str2HexStr(originalMessage);
        String decodedMessage = HexTranslate.HexStr2Str(hexMessage);

        assertEquals(originalMessage, decodedMessage);
    }

    @Test
    void testArrayReversal() {
        // 假设原始消息是 "1A"，转换为十六进制字符数组是 ['1', 'A']
        // 反转后应该是 ['A', '1']
        cryptor.decryptedCharArray = new char[] {'1', 'A'};
        cryptor.ReverseArrayToOriginalOrder();

        assertEquals('A', cryptor.decryptedCharArray[0]);
        assertEquals('1', cryptor.decryptedCharArray[1]);
    }
}
