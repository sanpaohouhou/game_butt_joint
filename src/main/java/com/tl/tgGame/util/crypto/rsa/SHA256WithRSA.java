package com.tl.tgGame.util.crypto.rsa;

public class SHA256WithRSA extends RSA {
    @Override
    public String getSign_algorithms() {
        return "SHA256WithRSA";
    }
}
