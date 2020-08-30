package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

@Slf4j
public class SignUtils {

    public static final String SIGN_ALGORITHM = "SHA256withRSA";

    /**
     * 获取签名
     * @param key 私钥
     * @param plain 待签名原文
     * @return 签名
     */
    public static String sign(PrivateKey key, String plain) {
        try {
            Signature sign = Signature.getInstance(SIGN_ALGORITHM);
            sign.initSign(key);
            sign.update(plain.getBytes(StandardCharsets.UTF_8));
            byte[] signInfo = sign.sign();
            return HexUtils.encodeHexString(signInfo);
        } catch (Exception e) {
            log.error("sign error", e);
            throw new CryptoException(e.getMessage(), e);
        }
    }

    /**
     * 验证签名
     * @param key 公钥
     * @param plain 待验证原文
     * @param signature 待验证签名
     * @return 是否验证通过
     */
    public static boolean verifySign(PublicKey key, String plain, String signature) {
        try {
            Signature sign = Signature.getInstance(SIGN_ALGORITHM);
            sign.initVerify(key);
            sign.update(plain.getBytes(StandardCharsets.UTF_8));
            byte[] signedBytes = HexUtils.decodeHex(signature);
            return sign.verify(signedBytes);
        } catch (Exception e) {
            log.error("verifySign error.", e);
            throw new CryptoException(e.getMessage(), e);
        }
    }
}
