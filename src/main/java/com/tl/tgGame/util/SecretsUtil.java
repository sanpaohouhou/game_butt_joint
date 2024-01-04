package com.tl.tgGame.util;

import java.security.SecureRandom;

public class SecretsUtil {

    public static SecureRandom getRandom(byte[] seed) {
        return (null == seed) ? new SecureRandom() : new SecureRandom(seed);
    }

    public static byte[] tokenBytes(int n) {
        byte[] token = new byte[n];
        getRandom(null).nextBytes(token);
        return token;
    }

    public static String tokenHex(int n) {
        return HexUtil.bytesToHexString(tokenBytes(n));
    }

    public static String tokenUrlSafe(int n) {
        return Base64Util.encodeUrlSafe(tokenBytes(n)).replace("=", "");
    }

    public static String token(int n) {
        return Base64Util.encode(tokenBytes(n)).replace("=", "");
    }

    public static void main(String[] args) {
    }
}
