package com.tl.tgGame.util.crypto.rsa;

public class MD5WithRSA extends RSA {
    @Override
    public String getSign_algorithms() {
        return "MD5WithRSA";
    }
}
