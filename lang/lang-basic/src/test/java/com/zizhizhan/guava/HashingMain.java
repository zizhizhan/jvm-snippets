package com.zizhizhan.guava;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 6/1/15
 *         Time: 10:58 AM
 */
public class HashingMain {

    public static void main(String[] args) {
        String text = "ADAFDASFDASFDASFDSFDSFADSFDSAFDASFDSAFSAFSAFDSAFDSAFSAFASFSAFSAFDSAFASFSAFASDF中国";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(text);
        }

        measure(Hashing.md5(), sb.toString(), "MD5");
        measure(Hashing.crc32(), sb.toString(), "CRC32");
        measure(Hashing.sha1(), sb.toString(), "SHA1");
        measure(Hashing.sha256(), sb.toString(), "SHA256");
        measure(Hashing.sha512(), sb.toString(), "SHA512");
    }


    public static void measure(HashFunction algorithm, String text, String label){
        for (int i = 0; i < 10; i++) {
            long begin = System.nanoTime();
            HashCode hscode = algorithm.hashString(text, StandardCharsets.UTF_8);
            long end = System.nanoTime();
            System.out.format("%s cost %d => %s\n", label, end - begin, hscode.toString().length());
        }
    }
}
