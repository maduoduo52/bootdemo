package com.mdd.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 14:15
 */
@Slf4j
public class AESCodeUtil {

    /**
     * 密钥，必须16位
     */
    private static Base64 base64 = new Base64();

    public static String encryptAES(String content, String key)
            throws Exception {
        byte[] byteContent = content.getBytes("UTF-8");
        // 注意，为了能与 iOS 统一
        // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");

        byte[] initParam = SecurityConstant.SECURITY_AES_KEY.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        // 指定加密的算法、工作模式和填充方式
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(byteContent);

        return new String(base64.encode(encryptedBytes));
    }

    public static String decryptAES(String content, String key)
            throws Exception {

        byte[] encryptedBytes = base64.decode(content);

        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");

        byte[] initParam = SecurityConstant.SECURITY_AES_KEY.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] result = cipher.doFinal(encryptedBytes);

        return new String(result, "UTF-8");
    }

    /**
     * 用于加密
     *
     * @param content
     * @return
     */
    public static String base64convert(String content) {
        String str1 = content.replaceAll("/", "_");
        return str1.replaceAll("\\+", "-");
    }

    /**
     * 用于解密
     *
     * @param content
     * @return
     */
    public static String base64convert2(String content) {
        String str1 = content.replaceAll("-", "\\+");
        return str1.replaceAll("_", "/");
    }

    public static String encode(String content, String key) {
        try {
            return base64convert(encryptAES(content, key));
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static String decode(String content, String key) {
        try {
            return decryptAES(base64convert2(content), key);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static String decode(String code) {
        return decode(code, SecurityConstant.SECURITY_AES_KEY);
    }

    public static String encode(String code) {
        return encode(code, SecurityConstant.SECURITY_AES_KEY);
    }
}
