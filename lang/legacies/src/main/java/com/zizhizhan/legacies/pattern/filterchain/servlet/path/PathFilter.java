package com.zizhizhan.legacies.pattern.filterchain.servlet.path;

import com.zizhizhan.legacies.pattern.filterchain.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@FilterConfig(order = 2)
public class PathFilter implements Filter {

    private final Map<String, PathCall> map = new HashMap<String, PathCall>();

    @SuppressWarnings("unchecked")
    public PathFilter() {
        try {
            Set<Class<?>> classes = ClassScanner.scan("com.zizhizhan.legacies.pattern.filterchain", Path.class);
            for (Class<?> clazz : classes) {
                log.info("add path {}", clazz);
                String parent = clazz.getAnnotation(Path.class).value();
                for (Method m : clazz.getMethods()) {
                    Object target = clazz.newInstance();
                    Path pa = m.getAnnotation(Path.class);
                    if (pa != null) {
                        log.info("add path {}", parent + pa.value());
                        map.put(parent + pa.value(), new PathCall(m, target));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Can't load path collection.", e);
        }
    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        PathCall pathcall = map.get(request.getResourcePath());
        if (pathcall != null) {
            try {
                ResponseBuilder.buildHeader(response);
                ResponseBuilder.buildBody(response, pathcall.call());
            } catch (Exception e) {
                log.warn("", e);
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private static class PathCall {

        final Method method;
        final Object target;

        public PathCall(Method method, Object target) {
            super();
            this.method = method;
            this.target = target;
        }

        String call() throws Exception {
            return method.invoke(target).toString();
        }
    }

}
