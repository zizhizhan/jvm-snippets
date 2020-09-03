package com.zizhizhan.legacies.pattern.singleton;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class CityEnums {

    //This sentence execute first than static block.
    private static final CityEnums CITYS = new CityEnums();

    private static Map<String, String> s_map;

    static {
        s_map = new HashMap<String, String>();
        s_map.put("0", "Beijing");
        System.out.println("Initialize class of CityEnums static block.");
    }

    public CityEnums() {
        System.out.println("Initialize instance of CityEnums.");
        initCitys();
    }

    private static void initCitys() {
        if (s_map == null) {
            s_map = new HashMap<String, String>();
        }
        s_map.put("1", "Shanghai");
        s_map.put("2", "Guangzhou");
        s_map.put("3", "Shenzhen");
    }

    public Map<String, String> getCache() {
        return Collections.unmodifiableMap(s_map);
    }

    public static CityEnums getInstance() {
        return CITYS;
    }

}
