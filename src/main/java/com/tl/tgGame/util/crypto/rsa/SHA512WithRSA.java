package com.tl.tgGame.util.crypto.rsa;

public class SHA512WithRSA extends RSA {
    @Override
    public String getSign_algorithms() {
        return "SHA512WithRSA";
    }
}
