package com.zizhizhan.legacies.pattern.proxy.dynamic;

import java.lang.reflect.Method;

public interface IHandler {

    void start(Method method);

    void end(Method method);

}
