package com.tl.tgGame.util;

import java.util.Base64;

public class Base64Util {
    public static String encodeUrlSafe(byte[] src) {
        return Base64.getUrlEncoder().encodeToString(src);
    }

    public static byte[] decodeUrlSafe(String src) {
        return Base64.getUrlDecoder().decode(src);
    }

    public static String encode(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    public static byte[] decode(String src) {
        return Base64.getDecoder().decode(src);
    }
}
