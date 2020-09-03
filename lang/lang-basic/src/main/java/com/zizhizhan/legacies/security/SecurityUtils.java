package com.zizhizhan.legacies.security;

import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;

public class SecurityUtils {

    private final static String[] SERVICE_NAMES = {"SecureRandom", "Signature", "KeyPairGenerator", "MessageDigest",
            "AlgorithmParameterGenerator", "AlgorithmParameters", "KeyFactory", "CertificateFactory", "KeyStore",
            "Policy", "Configuration", "CertPathBuilder", "CertPathValidator", "CertStore", "KeyManagerFactory",
            "TrustManagerFactory", "SSLContext", "Cipher", "KeyGenerator", "KeyAgreement", "SecretKeyFactory", "Mac",
            "GssApiMechanism", "SaslClientFactory", "SaslServerFactory", "TransformService", "XMLSignatureFactory",
            "KeyInfoFactory", "TerminalFactory"};

    private SecurityUtils() { }

    public static void showProviderInfo(boolean showAll) {
        for (Provider p : Security.getProviders()) {
            String providerInfo = String.format("{name: %s,  \tvesion: %s, \tinfo: %s, \t%s}", p.getName(), p
                    .getVersion(), p.getInfo(), p.getClass());
            System.out.println(providerInfo);
            if (showAll) {
                for (Service s : p.getServices()) {
                    System.out.printf("\t{type: %s, \talgorithm: %s, \tclass: %s}%n", s.getType(),
                            s.getAlgorithm(), s.getClassName());
                }
            }
        }
    }

    public static void showAlgorithms() {
        for (String type : SERVICE_NAMES) {
            System.out.println(type);
            for (String algorithm : Security.getAlgorithms(type)) {
                System.out.println("\t" + algorithm);
            }
        }
    }

    public static void main(String[] args) {
        showAlgorithms();
        showProviderInfo(true);
    }

}
