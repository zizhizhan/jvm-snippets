package com.zizhizhan.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import static com.zizhizhan.crypto.MessageDigests.*;

@Slf4j
public class MessageDigestsTests {

    @Test
    public void messageDigestForBytes() {
        String text = "Hello World";
        runChecksum(text, MessageDigests::md2sum, "md2sum");
        runChecksum(text, MessageDigests::md4sum, "md4sum");
        runChecksum(text, MessageDigests::md5sum, "md5sum");
        runChecksum(text, MessageDigests::shasum, "shasum");
        runChecksum(text, MessageDigests::sha224sum, "sha224sum");
        runChecksum(text, MessageDigests::sha256sum, "sha256sum");
        runChecksum(text, MessageDigests::sha384sum, "sha384sum");
        runChecksum(text, MessageDigests::sha512sum, "sha512sum");
    }

    @Test
    public void messageDigestForString() {
        String text = "Hello World";
        log.info("md2sum({}) = {}", text, md2sum(text));
        log.info("md4sum({}) = {}", text, md4sum(text));
        log.info("md5sum({}) = {}", text, md5sum(text));
        log.info("shasum({}) = {}", text, shasum(text));
        log.info("sha224sum({}) = {}", text, sha224sum(text));
        log.info("sha256sum({}) = {}", text, sha256sum(text));
        log.info("sha384sum({}) = {}", text, sha384sum(text));
        log.info("sha512sum({}) = {}", text, sha512sum(text));
    }

    private void runChecksum(String text, Function<byte[], byte[]> messageDigest, String algorithm) {
        byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
        byte[] checksum = messageDigest.apply(bytes);
        log.info("{} length: {} value: {}", algorithm, checksum.length, HexUtils.encodeHexString(checksum));
    }


}
