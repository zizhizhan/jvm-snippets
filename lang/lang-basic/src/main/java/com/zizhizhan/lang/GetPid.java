package com.zizhizhan.lang;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;

@Slf4j
public class GetPid {

    public static void main(String[] args) {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        log.info("JVM Name: {}.", jvmName);
        System.out.format("Current Pid = %s\n", jvmName.split("@")[0]);
    }

}
