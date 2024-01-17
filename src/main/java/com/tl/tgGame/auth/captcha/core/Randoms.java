package com.tl.tgGame.auth.captcha.core;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.Random;

/**
 * <p>随机工具类</p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public class Randoms {
    //定义验证码字符.去除了O和I等容易混淆的字母
    public static final char ALPHA[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'G', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            , 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 产生两个数之间的随机数
     *
     * @param min 小数
     * @param max 比min大的数
     * @return int 随机数字
     */
    public static int num(int min, int max) {
        return min + ThreadLocalRandom.current().nextInt(max - min);
    }

    /**
     * 产生0--num的随机数,不包括num
     *
     * @param num 数字
     * @return int 随机数字
     */
    public static int num(int num) {
        return ThreadLocalRandom.current().nextInt(num);
    }

    public static char alpha() {
        return ALPHA[num(0, ALPHA.length)];
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    public static void main(String[] args) {
        String s = generateRandomString(6);
        System.out.println(s);
    }
}