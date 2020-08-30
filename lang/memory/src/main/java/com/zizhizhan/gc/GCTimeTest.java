package com.zizhizhan.gc;

import java.util.HashMap;

/**
 * JVM ARGS:
 * 1. -Xmx512M -Xms512M -XX:+UseParNewGC
 * 2. -Xmx512M -Xms512M -XX:+UseParallelOldGC –XX:ParallelGCThreads=8
 * 3. -Xmx512M -Xms512M -XX:+UseSerialGC
 * 4. -Xmx512M -Xms512M -XX:+UseConcMarkSweepGC
 */
public class GCTimeTest {

    private static HashMap<Long, Object> map = new HashMap<>();

    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            if (map.size() * 512 / 1024 / 1024 >= 400) {
                map.clear();//保护内存不溢出
                System.out.println("clean map");
            }
            byte[] b1;
            for (int j = 0; j < 100; j++) {
                b1 = new byte[512];
                map.put(System.nanoTime(), b1);//不断消耗内存
            }
        }

        long timeEnd = System.currentTimeMillis();
        System.out.format("cost time is %s\n", timeEnd - timeStart);
    }
}


