package com.tl.tgGame.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {



    public static String MD5(String str ,int i){
        String substring;
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for(int j : digest){
                int j1 = j;
                if(j1 < 0){
                    j1 += 256;
                }

                if(j1 < 16){
                    stringBuffer.append("0");
                }

                stringBuffer.append(Integer.toHexString(j1));
            }
            if(i==16){
                substring = stringBuffer.toString().substring(8, 24);
            }else {
                substring = stringBuffer.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            substring = "";
        }
        return substring;
    }
}
