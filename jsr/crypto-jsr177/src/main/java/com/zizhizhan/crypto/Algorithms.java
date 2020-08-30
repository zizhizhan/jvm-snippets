package com.zizhizhan.crypto;

public interface Algorithms {

    String MD2 = "MD2";
    String MD4 = "MD4";
    String MD5 = "MD5";
    String SHA = "SHA-1";
    String SHA224 = "SHA-224";
    String SHA256 = "SHA-256";
    String SHA384 = "SHA-384";
    String SHA512 = "SHA-512";

    String MODE_CBC = "CBC";
    String MODE_ECB = "ECB";

    String PADDING_PKCS5 = "PKCS5PADDING";
    String PADDING_PKCS7 = "PKCS7Padding";

    String DES = "DES";
    String Triple_DES = "DESede";
    String AES = "AES";

    String DH = "DiffieHellman";
    String DSA = "DSA";
    String RSA = "RSA";
    String EC = "EC";

    String SHA1withDSA = "SHA1withDSA";
    String SHA224withDSA = "SHA224withDSA";
    String SHA256withDSA = "SHA256withDSA";
    String SHA384withDSA = "SHA384withDSA";
    String SHA512withDSA = "SHA512withDSA";

    String NONEwithRSA = "NONEwithRSA"; // rawRsa
    String MD5andSHA1withRSA = "MD5andSHA1withRSA";

    String MD2withRSA = "MD2withRSA";
    String MD5withRSA = "MD5withRSA";

    String SHA1withRSA = "SHA1withRSA";
    String SHA224withRSA = "SHA224withRSA";
    String SHA256withRSA = "SHA256withRSA";
    String SHA384withRSA = "SHA384withRSA";
    String SHA512withRSA = "SHA512withRSA";

    String NONEwithECDSA = "NONEwithECDSA";
    String SHA1withECDSA = "SHA1withECDSA";
    String SHA224withECDSA = "SHA224withECDSA";
    String SHA256withECDSA = "SHA256withECDSA";
    String SHA384withECDSA = "SHA384withECDSA";
    String SHA512withECDSA = "SHA512withECDSA";

}
