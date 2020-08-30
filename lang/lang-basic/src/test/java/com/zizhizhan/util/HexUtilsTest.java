package com.zizhizhan.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class HexUtilsTest {

    @Test
    public void bytes2Hex() {
        String hello = "Hello World";
        log.info("{} => {}", hello, HexUtils.bytes2Hex(hello.getBytes()));
    }

}
