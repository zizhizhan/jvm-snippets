package com.zizhizhan.crypto;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

@Slf4j
public class SymmetricCiphers implements Algorithms {

    private static final Map<String, SecretKeyGenerator> secretKeyGenerators = new HashMap<>();
    private static final Map<String, Integer> ivLength = new HashMap<>();

    static {
        secretKeyGenerators.put(DES, new DESSecretKeyGenerator());
        secretKeyGenerators.put(Triple_DES, new DESedeSecretKeyGenerator());
        secretKeyGenerators.put(AES, new AESSecretKeyGenerator());
        ivLength.put(DES, 8);
        ivLength.put(Triple_DES, 8);
        ivLength.put(AES, 16);
    }

    public static SecretKey generateSecretKey(String algorithm, int keysize) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(algorithm);
            generator.init(keysize);
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.warn("Invalid Algorithm {}", algorithm, e);
            throw new CryptoException("Invalid Algorithm: " + algorithm);
        }
    }

    public static String encrypt(String transformation, String password, String plaintext) {
        byte[] plainData = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] cipherData = doCrypto(new AlgorithmSpec(transformation), Cipher.ENCRYPT_MODE, password.getBytes(StandardCharsets.UTF_8), plainData);
        return Base64.getEncoder().encodeToString(cipherData);
    }

    public static String decrypt(String transformation, String password, String cipherText) {
        byte[] cipherData = Base64.getDecoder().decode(cipherText);
        byte[] plainData = doCrypto(new AlgorithmSpec(transformation), Cipher.DECRYPT_MODE, password.getBytes(StandardCharsets.UTF_8), cipherData);
        return new String(plainData, StandardCharsets.UTF_8);
    }

    private static byte[] doCrypto(AlgorithmSpec transformation, int opmode, byte[] password, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(transformation.toString());
            SecretKeyGenerator secretKeyGenerator = secretKeyGenerators.get(transformation.algorithm);
            SecretKey secretKey;
            if (secretKeyGenerator != null) {
                secretKey = secretKeyGenerator.generate(password);
            } else {
                secretKey = new SecretKeySpec(password, transformation.algorithm);
            }
            log.debug("MaxAllowedKeyLength for {}: {}", transformation, Cipher.getMaxAllowedKeyLength(transformation.algorithm));

            if (MODE_ECB.equalsIgnoreCase(transformation.mode) || Strings.isNullOrEmpty(transformation.mode)) {
                cipher.init(opmode, secretKey);
            } else {
                Integer length = ivLength.get(transformation.algorithm);
                IvParameterSpec zeroIv = new IvParameterSpec(length != null ? new byte[length] : new byte[16]);
                cipher.init(opmode, secretKey, zeroIv);
            }
            log.debug("[{}] with key {}/{}/{}.", transformation, secretKey.getAlgorithm(), secretKey.getFormat(),
                    secretKey.getEncoded().length * 8);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.info("Invalid transformation {}.", transformation, e);
            throw new CryptoException("Invalid transformation " + transformation, e);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            log.info("Invalid key of {}.", transformation, e);
            throw new CryptoException("Invalid key ", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.info("bad data of {}.", transformation, e);
            throw new CryptoException("Bad data", e);
        }
    }

    @FunctionalInterface
    interface SecretKeyGenerator {

        SecretKey generate(byte[] passwordBytes);

    }

    /**
     * AES私钥固定格式为128/192/256 bits。即：16/24/32bytes。
     */
    static class AESSecretKeyGenerator implements SecretKeyGenerator {
        @Override
        public SecretKey generate(byte[] passwordBytes) {
            if (passwordBytes.length < 16) {
                return new SecretKeySpec(MessageDigests.md5sum(passwordBytes), AES);
            } else if (passwordBytes.length < 24) {
                return new SecretKeySpec(MessageDigests.hash192(passwordBytes), AES);
            } else {
                return new SecretKeySpec(MessageDigests.sha256sum(passwordBytes), AES);
            }
        }
    }

    static abstract class DefaultSecretKeyGenerator implements SecretKeyGenerator {

        @Override
        public SecretKey generate(byte[] passwordBytes) {
            String algorithm = getAlgorithm();
            try {
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
                return keyFactory.generateSecret(generateKeySpec(passwordBytes));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
                String password =  new String(passwordBytes, StandardCharsets.UTF_8);
                log.info("Can't create secret key for {} with key {}.", algorithm, password, e);
                throw new CryptoException("Can't create secret key for " + algorithm + " with key " + password + ".", e);
            }
        }

        protected abstract String getAlgorithm();
        protected abstract KeySpec generateKeySpec(byte[] passwordBytes) throws InvalidKeyException;
    }

    /**
     * 密钥为8位。
     */
    static class DESSecretKeyGenerator extends DefaultSecretKeyGenerator {

        @Override
        protected String getAlgorithm() {
            return DES;
        }

        @Override
        protected KeySpec generateKeySpec(byte[] passwordBytes) throws InvalidKeyException {
            return new DESKeySpec(MessageDigests.md5sum(passwordBytes));
        }
    }

    /**
     * 密钥为24位。
     */
    static class DESedeSecretKeyGenerator extends DefaultSecretKeyGenerator {

        @Override
        protected String getAlgorithm() {
            return Triple_DES;
        }

        @Override
        protected KeySpec generateKeySpec(byte[] passwordBytes) throws InvalidKeyException {
            return new DESedeKeySpec(MessageDigests.hash192(passwordBytes));
        }
    }

    static class AlgorithmSpec {

        final String algorithm;
        final String mode;
        final String padding;

        public AlgorithmSpec(String transformation) {
            List<String> list = Splitter.on("/").splitToList(transformation);
            if (list.isEmpty()) {
                this.algorithm = transformation;
                this.mode = null;
                this.padding = null;
            } else {
                this.algorithm = list.get(0);
                if (list.size() > 1) {
                    this.mode = list.get(1);
                } else {
                    this.mode = null;
                }
                if (list.size() > 2) {
                    this.padding = list.get(2);
                } else {
                    this.padding = null;
                }
            }
        }

        public AlgorithmSpec(String algorithm, String mode, String padding) {
            this.algorithm = algorithm;
            this.mode = mode;
            this.padding = padding;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AlgorithmSpec that = (AlgorithmSpec) o;
            return Objects.equals(algorithm, that.algorithm) &&
                    Objects.equals(mode, that.mode) &&
                    Objects.equals(padding, that.padding);
        }

        @Override
        public int hashCode() {
            return Objects.hash(algorithm, mode, padding);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.algorithm);
            if (!Strings.isNullOrEmpty(this.mode)) {
                sb.append('/').append(this.mode);
            }
            if (!Strings.isNullOrEmpty(this.padding)) {
                sb.append('/').append(this.padding);
            }
            return sb.toString();
        }
    }

}
