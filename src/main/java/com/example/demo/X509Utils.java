package com.example.demo;

import java.security.cert.X509Certificate;
import java.util.Base64;

public class X509Utils {
    private X509Utils() {
    }

    public static String getBase64PublicKey(X509Certificate cert) {
        return cert == null ? null : Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded());
    }
}
