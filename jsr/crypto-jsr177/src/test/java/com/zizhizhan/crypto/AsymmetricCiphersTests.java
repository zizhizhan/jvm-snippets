package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class AsymmetricCiphersTests implements Algorithms {

    @Test
    public void signVerify() throws Exception {
        String[] dsaAlgorithms = { SHA1withDSA, SHA224withDSA, SHA256withDSA };
        doSignVerify(DSA, 1024, dsaAlgorithms);

        String[] rsaAlgorithms = { NONEwithRSA, MD5andSHA1withRSA, MD2withRSA, MD5withRSA, SHA1withRSA,  SHA224withRSA,
                SHA256withRSA, SHA384withRSA, SHA512withRSA };
        doSignVerify(RSA, 1024, rsaAlgorithms);

        String[] ecAlgorithms = { NONEwithECDSA, SHA1withECDSA, SHA224withECDSA,  SHA256withECDSA, SHA384withECDSA,
                SHA512withECDSA };
        doSignVerify(EC, 256, ecAlgorithms);
    }

    private void doSignVerify(String algorithm, int keysize, String[] signatureAlgorithms) throws NoSuchAlgorithmException {
        byte[] plainBytes = "Hello James".getBytes(StandardCharsets.UTF_8);
        KeyPair keyPair = RsaUtils.generateKeyPair(algorithm, keysize);
        for (String a : signatureAlgorithms) {
            byte[] signatureBytes = AsymmetricCiphers.sign(a, keyPair.getPrivate(), plainBytes);
            String signature = HexUtils.encodeHexString(signatureBytes);
            log.info("[{}] signature: {}", a, signature);

            boolean verified = AsymmetricCiphers.verify(a, keyPair.getPublic(), plainBytes, HexUtils.decodeHex(signature));
            log.info("verified: {}.", verified);
        }
    }

}
