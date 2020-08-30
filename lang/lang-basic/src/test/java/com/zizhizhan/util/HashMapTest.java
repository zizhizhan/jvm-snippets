package com.zizhizhan.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class HashMapTest {

    /**
     * 当key没有实现Comparable，将穷举遍历左右二叉树逐个比较key是否相等。
     *
     * HashMap.TreeNode#find
     */
    @Test
    public void testRBTree() {
        HashMap<Key, Integer> map = new HashMap<>();

        for (int i = 0; i < 1000; i++) {
            Key key = new Key("hello", i, i);
            map.put(key, i);
        }

        List<Key> keyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Key key = new Key("hello", 1, 2L);
            keyList.add(key);
            map.put(key, i);
        }

        for (int i = 10; i < 20; i++) {
            Key key = new Key("hello", 2, 1L);
            keyList.add(key);
            map.put(key, i);
        }

        for (Key key : keyList) {
            log.info("{}: {}", key, map.get(key));
        }
    }

}
