package com.zizhizhan.reflections;

import com.google.common.base.Function;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/23
 * Time: 上午9:48
 */
public class ClassScannerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassScannerTest.class);

    @Test
    public void scanClasses() throws Exception {
        Reflections platformReflections = new Reflections("com.google");
        LOGGER.info("Store is {}.", getStoreMap(platformReflections.getStore()));
        Set<Class<? extends Function>> classes = platformReflections.getSubTypesOf(Function.class);
        LOGGER.info("Store is {}.", getStoreMap(platformReflections.getStore()));
        LOGGER.info("SubClass of Function is {}.", classes);
    }

    private Object getStoreMap(Store store) throws NoSuchFieldException, IllegalAccessException {
        Field field = store.getClass().getDeclaredField("storeMap");
        field.setAccessible(true);
        return field.get(store);
    }

}
