package com.cyh.util;

import com.alibaba.druid.filter.config.ConfigTools;
import com.cyh.exception.MyException;

import java.math.BigInteger;
import java.security.MessageDigest;

public final class EncryptionAndDecryptionUtil {
    private static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMi+PgjLKxWgeyrG+bd73VBgELAG/HeeQKZxYzkfzWhCi+CMWgsIMksa0PfIAwjnGVpinLVSQ0XRYWLQ1uYtAhcCAwEAAQ==";
    private static final String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAyL4+CMsrFaB7Ksb5t3vdUGAQsAb8d55ApnFjOR/NaEKL4IxaCwgySxrQ98gDCOcZWmKctVJDRdFhYtDW5i0CFwIDAQABAkEAtKlqIPXf543Pazc5pCUIj351ZkZTTsAQxPrE5rM1ZH7sonH3KQnU5CGd93W8lAVJg3mUOe0IT29tRFUkx7oUIQIhAP6dMP4CJ6Bw6l6rNOn70+LDKYH1oiaNAlLlk6kBfVbxAiEAydX7I6qHyICQAP4+urfh9M8Wc8JVnlqh4zZavJ1AuYcCIDD8i02Dmp4qu1zlcbPG6LhLNjQzzzO9k4Hhe6xbhgLxAiADhfC0Vq24GkZpPgbqS3tuQo8pxiKoePyKctByqmwO0QIgJKiaN80Ubuu/iho9FhHX3ojTbOlASPrxSx1MQhFguVw=";
    //MD5加密
    private static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getEncryptionPassword(String password) throws MyException {
        try {
            return ConfigTools.encrypt(privateKey, getMD5(password));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("服务器内部错误！");
        }
    }

    public static void main(String[] args) throws MyException {
        System.out.println(getEncryptionPassword("cyh19930807"));
    }
}
