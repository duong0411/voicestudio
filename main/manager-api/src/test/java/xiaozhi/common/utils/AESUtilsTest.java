package xiaozhi.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AESUtilsTest {

    @Test
    public void testEncryptAndDecrypt() {
        String key = "xiaozhi1234567890";
        String plainText = "Hello, !";

        System.out.println(": " + plainText);
        System.out.println(": " + key);


        String encrypted = AESUtils.encrypt(key, plainText);
        System.out.println(": " + encrypted);


        String decrypted = AESUtils.decrypt(key, encrypted);
        System.out.println(": " + decrypted);


        assertEquals(plainText, decrypted, "");
        System.out.println(": " + plainText.equals(decrypted));
    }

    @Test
    public void testDifferentKeyLengths() {
        String[] keys = {
                "1234567890123456",
                "123456789012345678901234",
                "12345678901234567890123456789012",
                "short",
                "verylongkeythatwillbetruncatedto32bytes"
        };

        String plainText = "";

        for (String key : keys) {
            String encrypted = AESUtils.encrypt(key, plainText);
            String decrypted = AESUtils.decrypt(key, encrypted);
            assertEquals(plainText, decrypted, ": " + key.length());
        }
    }

    @Test
    public void testSpecialCharacters() {
        String key = "xiaozhi1234567890";
        String[] testTexts = {
                "Hello World",
                "",
                "Hello, !",
                ": !@#$%^&*()",
                "123",
                "Emoji: 😀🎉🚀",
                "",
                ""
        };

        for (String text : testTexts) {
            String encrypted = AESUtils.encrypt(key, text);
            String decrypted = AESUtils.decrypt(key, encrypted);
            assertEquals(text, decrypted, ": " + text);
        }
    }

    @Test
    public void testCrossLanguageCompatibility() {

        String key = "xiaozhi1234567890";
        String plainText = "Hello, !";



        // String decrypted = AESUtils.decrypt(key, pythonEncrypted);



        String javaEncrypted = AESUtils.encrypt(key, plainText);
        System.out.println("JavaPython: " + javaEncrypted);
    }
}