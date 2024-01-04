package com.tl.tgGame.project.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/1 , 18:42
 */
@Component
public class AesGameUtil {

    /**
     * 加密
     * @param dataString
     * @param appKey
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String dataString, String appKey) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        SecretKeySpec keySpec = new SecretKeySpec(appKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return encoder.encodeToString(cipher.doFinal(dataString.getBytes("UTF-8")));
    }

    /**
     * 解密
     * @param dataString
     * @param appKey
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String dataString, String appKey) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        SecretKeySpec keySpec = new SecretKeySpec(appKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance( "AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(decoder.decode(dataString)));
    }

}
