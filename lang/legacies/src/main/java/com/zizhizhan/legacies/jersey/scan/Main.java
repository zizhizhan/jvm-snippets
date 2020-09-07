package com.zizhizhan.legacies.jersey.scan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class Main {

    public static void main(String[] args) throws IOException {
        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.property.packages", "com.zizhizhan.legacies.jersey");

        GrizzlyWebContainerFactory.create("http://localhost:8088/", initParams);
    }

}
