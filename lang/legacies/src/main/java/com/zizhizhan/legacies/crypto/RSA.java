package com.zizhizhan.legacies.crypto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSA {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        KeyPair pair = generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // Save Pirvate Key
        FileOutputStream f1 = new FileOutputStream("private.ppk");
        ObjectOutputStream b1 = new ObjectOutputStream(f1);
        b1.writeObject(privateKey);

        // Save Public Key
        FileOutputStream f2 = new FileOutputStream("public.ppk");
        ObjectOutputStream b2 = new ObjectOutputStream(f2);
        b2.writeObject(publicKey);

        encrypt();
        decrypt();
    }

    public static void encrypt() throws IOException, ClassNotFoundException {
        String msg = "Hello World!";
        FileInputStream f = new FileInputStream("public.ppk");
        ObjectInputStream b = new ObjectInputStream(f);
        RSAPublicKey pbk = (RSAPublicKey) b.readObject();

        // RSA算法是使用整数进行加密的，在RSA公钥中包含有两个整数信息：e和n。对于明文数字m,计算密文的公式是m的e次方再与n求模。
        BigInteger e = pbk.getPublicExponent();
        BigInteger n = pbk.getModulus();

        // 获取明文的大整数
        byte ptext[] = msg.getBytes("utf-8");
        BigInteger m = new BigInteger(ptext);

        // 加密明文
        BigInteger c = m.modPow(e, n);

        // 打印密文c
        System.out.println("c= " + c);

        // 将密文以字符串形式保存在文件中
        String cs = c.toString();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("encrypt.dat"), "utf-8"));
        out.write(cs, 0, cs.length());
        out.close();

    }

    public static void decrypt() throws IOException, ClassNotFoundException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("encrypt.dat"), "utf-8"));
        String ctext = in.readLine();
        BigInteger c = new BigInteger(ctext);

        // 获取私钥
        FileInputStream f = new FileInputStream("private.ppk");
        ObjectInputStream b = new ObjectInputStream(f);
        RSAPrivateKey prk = (RSAPrivateKey) b.readObject();

        // 获取私钥的参数d,n
        BigInteger d = prk.getPrivateExponent();
        BigInteger n = prk.getModulus();

        // 解密明文
        BigInteger m = c.modPow(d, n);


        // 计算明文对应的字符串并输出。
        byte[] mt = m.toByteArray();
        System.out.println("PlainText is " + new String(mt, "utf-8"));
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        final int KEY_SIZE = 1024;
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        return keyPairGen.genKeyPair();
    }

}
