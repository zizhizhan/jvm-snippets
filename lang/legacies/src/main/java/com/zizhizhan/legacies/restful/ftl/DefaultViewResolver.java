package com.zizhizhan.legacies.restful.ftl;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zizhizhan.legacies.restful.party.entity.Person;

@FtlAssemble
public class DefaultViewResolver implements ViewResolver {

    private final Map<Class<?>, String> map = new HashMap<>();

    public DefaultViewResolver() {
        map.put(List.class, "list.ftl");
        map.put(Person.class, "person.ftl");
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String resolveViewName(Object t, Class<?> type, OutputStream entityStream) {
        for (Class<?> clazz : map.keySet()) {
            if (clazz.isAssignableFrom(type)) {
                return map.get(clazz);
            }
        }
        return "default.ftl";
    }

}
