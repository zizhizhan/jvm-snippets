package com.zizhizhan.reflect;

import com.zizhizhan.legacies.spring.AnnotationUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.lang.reflect.Method;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/24
 * Time: 下午5:03
 */
public class BasicClassMethodTest {

    @Test
    public void testAnnotationMethods() throws NoSuchMethodException {
        Method helloMethod = MyTestClass.class.getMethod("hello");
        Assert.assertNull(helloMethod.getAnnotation(WebMethod.class));
        Assert.assertNotNull(AnnotationUtils.findAnnotation(helloMethod, WebMethod.class));
        Assert.assertEquals(AnnotationUtils.findAnnotation(helloMethod, WebMethod.class).annotationType(), WebMethod.class);
    }

    @Test
    public void testAnnotationClass() {
        Assert.assertNull(MyTestClass.class.getAnnotation(WebService.class));
        Assert.assertNotNull(AnnotationUtils.findAnnotation(MyTestClass.class, WebService.class));
        Assert.assertEquals(AnnotationUtils.findAnnotation(MyTestClass.class, WebService.class).annotationType(), WebService.class);
    }

    @WebService
    interface WithAnnotationMethods {
        @WebMethod
        String hello();
    }

    static class MyTestClass implements WithAnnotationMethods {
        @Override
        public String hello() {
            return "hello";
        }

        public String world() {
            return "world";
        }
    }



}
