package com.zizhizhan.guava;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/2
 *         Time: PM6:25
 */
@Slf4j
public class FuncUsage {

    public static void main(String[] args) {
        Map<String, Integer> map = ImmutableMap.of("a", 1, "b", 2, "c", 3);
        Function<String, Integer> lookup = Functions.forMap(map, null);

        log.info("value is {}.", lookup.apply("a"));
        log.info("value is {}.", lookup.apply("e"));
    }
}
