package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.Base64;

@Slf4j
public class CertificateTests {

    //@Test
//    public void loadRSAPrivateKey() throws IOException {
//        byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath("/Users/james/Desktop/id_rsa"));
//        System.out.println(Arrays.toString(bytes));
//        System.out.println(new String(bytes));
//        CertificateUtils.loadPrivateKey(bytes, "PKCS12", "", "id_rsa", "");
//    }

    @Test
    public void loadRSAPublicKey() throws IOException {
        byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath("/opt/etc/zizhizhan-pubkey.cert"));
        PublicKey publicKey = CertificateUtils.loadX509PublicKey(bytes);
        System.out.println(new String(bytes));
        log.info("Algorithm: {}/{}\n{}", publicKey.getAlgorithm(), publicKey.getFormat(), publicKey);
        log.info("key: {}", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    }

    @Test
    public void loadRSAPrivateKey() throws Exception {
//        byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath("/opt/etc/zizhizhan-prikey.cert"));
//        System.out.println(new String(bytes));
//        CertificateUtils.loadPrivateKey(bytes, "PKCS12", "", "id_rsa", "");
        KeyStore keyStore = KeyStore.getInstance("PKCS#8");
        try (FileInputStream fis = new FileInputStream("/opt/etc/zizhizhan-prikey.cert")) {
            keyStore.load(fis, null);
        }
        System.out.println("keystore type=" + keyStore.getType());
    }
}
