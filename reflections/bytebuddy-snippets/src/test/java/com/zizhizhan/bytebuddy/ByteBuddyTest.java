package com.zizhizhan.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/22
 * Time: 下午9:38
 */
public class ByteBuddyTest {

    @Test
    public void subclass() throws Exception {
        Class<? extends TestService> proxyType = new ByteBuddy().subclass(TestService.class)
                        .method(ElementMatchers.named("invoke"))
                        .intercept(MethodDelegation.to(new TestInterceptor(who -> "Hello " + who)))
                        .make()
                        .load(this.getClass().getClassLoader())
                        .getLoaded();
        TestService testService = proxyType.newInstance();
        Assert.assertEquals("Hello James", testService.invoke("James"));
        Assert.assertEquals("Hello James", testService.apply("James"));
    }

    public interface TestService extends Function<String, String> {

        String invoke(String arg);

        default String apply(String arg) {

            return invoke(arg);

        }
    }

    public static class TestInterceptor {

        private final Function<Object, Object> function;

        public TestInterceptor(Function<Object, Object> function) {
            this.function = function;
        }

        @RuntimeType
        public Object intercept(@AllArguments Object[] allArguments) {
            Object arg = allArguments[0];
            return function.apply(arg);
        }

    }

}
