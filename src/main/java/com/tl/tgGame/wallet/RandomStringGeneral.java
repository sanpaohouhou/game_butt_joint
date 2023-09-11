package com.tl.tgGame.wallet;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class RandomStringGeneral {
    private static final int num = 43;
    private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    @Deprecated
    private static final char[] c = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    public static String general() {
        SecureRandom random;
        random = new SecureRandom();
        char[] result = new char[num];
        for (int i = 0; i < num; i++)
            result[i] = ALPHABET[random.nextInt(ALPHABET.length)];
        return new String(result);
    }

    public static String randomint() {
        return String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1000000));
    }

    public static void main(String[] args) {
        System.out.println(randomint());
    }

}