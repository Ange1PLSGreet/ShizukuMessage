package hex;

import org.cookiebyte.dev.cryptor.HexTranslate;
import org.junit.jupiter.api.Test;

public class HexTranslateTest {

    @Test
    public void HexTranslate() {
        // 示例字符串
        String originalString = "中文测试";

        // 字符串转十六进制
        String hexString = HexTranslate.Str2HexStr(originalString);
        System.out.println("原始字符串: " + originalString);
        System.out.println("转换为十六进制: " + hexString);

        // 移除空格，准备转换回字符串
        String cleanHexString = hexString.replace(" ", "");

        // 十六进制转回字符串
        String restoredString = HexTranslate.HexStr2Str(cleanHexString);
        System.out.println("从十六进制恢复: " + restoredString);
    }

}
