package me.jameszhan.pattern.base;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/19
 * Time: 上午11:36
 */
final class MethodIdentifier {

    private final String name;
    private final List<Class<?>> parameterTypes;

    MethodIdentifier(Method method) {
        this.name = method.getName();
        this.parameterTypes = Arrays.asList(method.getParameterTypes());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{name, parameterTypes});
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MethodIdentifier) {
            MethodIdentifier ident = (MethodIdentifier) o;
            return name.equals(ident.name) && parameterTypes.equals(ident.parameterTypes);
        }
        return false;
    }
}
