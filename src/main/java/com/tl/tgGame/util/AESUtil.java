package com.tl.tgGame.util;

import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Log4j2
public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    
    /**
     * 随机生成密钥
     *
     * @return
     */
    public static String getAESRandomKey() {
        SecureRandom random = new SecureRandom();
        long randomKey = random.nextLong();
        return String.valueOf(randomKey);
    }

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) throws Exception {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return Base64Util.encode(result);
        } catch (Exception ex) {
            log.error("加密失败", ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, String key) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
            //执行操作
            byte[] result = cipher.doFinal(Base64Util.decode(content));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error("解密失败", ex);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static Key getSecretKey(final String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        try {
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // 此类提供加密的强随机数生成器 (RNG)，该实现在windows上每次生成的key都相同，但是在部分linux或solaris系统上则不同。
            // SecureRandom random = new SecureRandom(key.getBytes());
            // 指定算法名称，不同的系统上生成的key是相同的。
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());
            //AES 要求密钥长度为 128
            kg.init(128, random);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            log.info("生成加密秘钥异常！", ex);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String key = "U81msjdw0v486i+ol8pko+0_(m0ck-p6%om!wbkmg0_(.34&1";
        System.out.println(encrypt("{\"password\": \"begin chef cost scare off record meadow arena yard monitor donate lecture\", \"ethPriKey\": \"937eb51f179eb049e05bc4206271ec8d622263722f5677ac96483042dc6d7ad6\", \"ethAddress\": \"0x605BfA292B84bC05E044a015BfFBC690B3915B05\", \"tronPriKey\": \"0b15b3b7e9203be8355723a6d00fed4f3a54e8990ffb37a5fdfe79fbb053b4b3\", \"tronAddress\": \"TC7UCyGzERTtLuTgjjp28PRSZaQsyXHv8m\"}", key));
        System.out.println(decrypt("b+5yQavt+fKPWWW7O3YGxMyjxsCR8cH4Y/lN6V6ThWjPJM98Sk3RhxzVZwyq+DJuMLGQXC0BFIMAleXNCWZDakbKOACdAyXjgUp8mQPaMIFSnW9724E/XFM6kloqsgPglbQ3DY4+8/BRXvRvohU8OVxJa31JiFFtJxaQYksvGkdce75EF+7D5ibOrex5mPXPOSOA71k0R698NvL31MPIx2lrV3Qx8FX7h+P5SlZvywy6fOuCVI+V6hlDPmRS1GnpxJZ94FxBSgreCPQTgRD8+/lSvczb86INQ2iAieBRR9DIPUmpZw6ixg9h0TiXBwdvtM/YFANfAAzvyPqQcnQ/HiRQLdhK9ceEBf801vbGXd+kCWB4SJk6d+bqeTi64qBM1e1IFSNO9rTbYBzF15eqK+7OCW25FYp/FfWPmXkK5FT6FWPjLY6MqJQjtKO/9Jd3hOdiCGw2RX2C0ghnDR2L+z7klqsJBPNs27p0JHTUjxM=", key));
    }
}