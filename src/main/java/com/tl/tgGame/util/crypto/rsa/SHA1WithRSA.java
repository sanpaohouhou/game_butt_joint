package com.tl.tgGame.util.crypto.rsa;

public class SHA1WithRSA extends RSA {
    @Override
    public String getSign_algorithms() {
        return "SHA1WithRSA";
    }
}
