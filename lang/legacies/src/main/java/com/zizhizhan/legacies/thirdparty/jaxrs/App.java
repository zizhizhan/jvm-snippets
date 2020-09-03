package com.zizhizhan.legacies.thirdparty.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class App extends Application {

    @Override
    public Set<Object> getSingletons() {
        Set<Object> classes = new HashSet<>();
        classes.add(new Users());
        return classes;
    }

}
