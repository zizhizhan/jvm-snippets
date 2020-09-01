package com.zizhizhan.legacies.crypto;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class DesCrypto {

    // Values for SUN DES local Encryption
    private static final String CRYPTO_PROVIDER = "SunJCE";
    private static final String CIPHER_NAME = "DESede/ECB/PKCS5Padding";
    private static final String SECRETKEY_FACTORY = "DESede";

    private static final String ENCODING_UTF8 = "UTF-8";

    private String m_username;
    private SecretKey m_secretKey;

    private byte[] m_cipherTextBytePrefix;
    private byte[] m_clearTextByteSuffix;

    public DesCrypto(String passphrase) {
        setPassphrase(passphrase);
    }

    public static void main(String[] args) {
        DesCrypto crypto = new DesCrypto("a7ewk'er4tgm5j3w +[e4<>^*(@#,ssssg +");
        // String pass = "AQAQAAEAEAAxMTAxM0aEj0jHQhNSX38buIG0k2Q=";
        // System.out.println(crypto.decryptLocalString(pass));
        String x;
        System.out.println((x = crypto.encryptLocalString("SFTPTestUser$")));
        System.out.println(crypto.decryptLocalString(x));
    }

    public void setUsername(String username) {
        m_username = username;
    }

    public String getUsername() {
        return m_username;
    }

    public void setPassphrase(String passphrase) throws CryptoException {
        try {
            // init secret key
            DESedeKeySpec keySpec = new DESedeKeySpec(passphrase.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(
                    SECRETKEY_FACTORY, CRYPTO_PROVIDER);
            m_secretKey = secretKeyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            throw new CryptoException(
                    "Failed to generate the secret key from the passphrase.", e);
        }
    }

    public void setCipherTextBytePrefix(String byteString) {
        if (byteString != null && byteString.trim().length() > 0) // NOPMD
        {
            String[] bytes = byteString.split(",");
            m_cipherTextBytePrefix = new byte[bytes.length];
            for (int s = 0; s < bytes.length; s++) {
                m_cipherTextBytePrefix[s] = Byte.decode(bytes[s]);
            }
        } else {
            m_cipherTextBytePrefix = new byte[0];
        }
    }

    public void setClearTextByteSuffix(String byteString) {
        if (byteString != null && byteString.trim().length() > 0) // NOPMD
        {
            String[] bytes = byteString.split(",");
            m_clearTextByteSuffix = new byte[bytes.length];
            for (int s = 0; s < bytes.length; s++) {
                m_clearTextByteSuffix[s] = Byte.decode(bytes[s]);
            }
        } else {
            m_clearTextByteSuffix = new byte[0];
        }
    }

    /**
     * Internal to the crypto module
     */
    public String encryptLocalString(String data) {
        byte[] dataEncrypted = processLocalBytes(data.getBytes(),
                Cipher.ENCRYPT_MODE);
        String dataEncryptedBase64 = new String(Base64.encode(dataEncrypted));

        return dataEncryptedBase64;
    }

    /**
     * Internal to the crypto module
     */
    public String decryptLocalString(String data) {
        byte[] dataDecoded = Base64.decode(data);
        byte[] dataDecrypted = processLocalBytes(dataDecoded,
                Cipher.DECRYPT_MODE);
        String clearText = new String(dataDecrypted);

        return clearText;
    }

    private byte[] processLocalBytes(byte[] data, int opmode)
            throws CryptoException {
        byte[] dataEncrypted = null;

        try {
            // Create the cipher
            Cipher desCipher = Cipher.getInstance(CIPHER_NAME, CRYPTO_PROVIDER);
            // Initialize the cipher for encryption
            desCipher.init(opmode, m_secretKey);
            // Encrypt the ciphertext
            dataEncrypted = desCipher.doFinal(data);
        } catch (Exception e) {
            throw new CryptoException("Failed to local process the data.", e);
        }

        return dataEncrypted;
    }

    /**
     * Append two byte arrays together.
     *
     * @param array1
     * @param array2
     * @return
     */
    private byte[] append(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];
        int r = 0;

        for (int i = 0; i < array1.length; i++) // NOPMD
        {
            result[r] = array1[i];
            r++;
        }

        for (int i = 0; i < array2.length; i++) // NOPMD
        {
            result[r] = array2[i];
            r++;
        }

        return result;
    }

    /**
     * Return a new byte array containing just the specified element section.
     *
     * @param array
     * @param start Element to start copying.
     * @param end   Element to stop copying. This is the index of the element
     *              after the final element.
     * @return The array slice in a new byte array.
     */
    private byte[] slice(byte[] array, int start, int end) {
        int length = end - start;
        if (length < 0) {
            throw new CryptoException("Invalid positions for slice at " + start
                    + " and " + end);
        }

        byte[] result = new byte[length];
        int r = 0;
        for (int i = start; i < end; i++) // NOPMD
        {
            result[r] = array[i];
            r++;
        }

        return result;
    }

    protected String addSuffix(String clearText) {
        byte[] augmentedBytes;
        String augmentedText = null;
        try {
            augmentedBytes = append(clearText.getBytes(ENCODING_UTF8),
                    m_clearTextByteSuffix);
            augmentedText = Base64.encode(augmentedBytes);
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException("unsupported encoding.", e);
        }

        return augmentedText;
    }

    protected String removeSuffix(String base64Text) {
        byte[] base64Bytes = Base64.decode(base64Text);
        String base64String = null;

        // only remove the prefix if it is present
        int start = base64Bytes.length - m_clearTextByteSuffix.length;
        boolean matches = (start >= 0);
        for (int b = start, s = 0; s < m_clearTextByteSuffix.length && matches; b++, s++) {
            matches = (base64Bytes[b] == m_clearTextByteSuffix[s]);
        }

        if (matches) {
            base64Bytes = slice(base64Bytes, 0, start);
        }
        try {
            base64String = new String(base64Bytes, ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException("unsupported encoding.", e);
        }
        return base64String;
    }

    public class EncodingException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public EncodingException(String msg, Throwable e) {
            super(msg, e);
        }
    }
}