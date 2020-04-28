package com.example.demo.util;

import java.security.cert.X509Certificate;
import java.util.Base64;

public class X509Util {
    private X509Util() {
    }

    public static String getBase64PublicKey(X509Certificate cert) {
        return cert == null ? null : Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded());
    }
}
