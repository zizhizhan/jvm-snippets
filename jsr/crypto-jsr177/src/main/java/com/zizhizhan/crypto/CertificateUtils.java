package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Slf4j
public final class CertificateUtils {

    public static PublicKey loadX509PublicKey(byte[] certificateFileBytes) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            try(InputStream in = new ByteArrayInputStream(certificateFileBytes)) {
                Certificate certificate = certificateFactory.generateCertificate(in);
                return certificate.getPublicKey();
            }
        } catch (CertificateException | IOException e) {
            log.error("loadX509PublicKey error", e);
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static PublicKey loadPublicKey(byte[] fileBytes, String type, String password, String alias) {
        KeyStore keyStore = loadKeyStore(fileBytes, type, password);
        try {
            return keyStore.getCertificate(alias).getPublicKey();
        } catch (KeyStoreException e) {
            log.error("loadPublicKey error", e);
            throw new CryptoException(e.getMessage(), e);
        }
    }

    public static PrivateKey loadPrivateKey(byte[] fileBytes, String type, String password, String alias, String privateKeyPassword) {
        KeyStore keyStore = loadKeyStore(fileBytes, type, password);
        try {
            return (PrivateKey)keyStore.getKey(alias, privateKeyPassword.toCharArray());
        } catch (Exception e) {
            log.error("loadPrivateKey error",e);
            throw new CryptoException(e.getMessage(), e);
        }
    }

    private static KeyStore loadKeyStore(byte[] fileBytes, String type, String password) {
        try(InputStream in = new ByteArrayInputStream(fileBytes)) {
            KeyStore keyStore = KeyStore.getInstance(type);
            keyStore.load(in, password.toCharArray());
            return keyStore;
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            log.error("loadKeyStore Error", e);
            throw new CryptoException(e.getMessage(), e);
        }
    }
}
