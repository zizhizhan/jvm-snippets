package com.zizhizhan.legacies.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class HtmlUtils {

    static final char REFERENCE_START = '&';
    static final String DECIMAL_REFERENCE_START = "&#";
    static final String HEX_REFERENCE_START = "&#x";
    static final char REFERENCE_END = ';';
    static final char CHAR_NULL = (char) -1;

    private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";
    private static final Map<String, Character> ENTITY_REF_CHARACTER_MAP = new HashMap<String, Character>(252);
    private static final Pattern ENTITY_REF_PATTERN = Pattern.compile("\\&([^;]+);");

    static {
        try {
            initialize();
        } catch (Throwable t) {
            log.warn("Can't initialize the HtmlUtils", t);
        }
    }

    private HtmlUtils() {
    }


    private static void initialize() {
        Properties entityReferences = new Properties();
        // Load refeence definition file.
        InputStream is = HtmlUtils.class.getResourceAsStream(PROPERTIES_FILE);
        if (is == null) {
            throw new IllegalStateException(
                    "Cannot find reference definition file [HtmlCharacterEntityReferences.properties] as class path resource");
        }
        try {
            try {
                entityReferences.load(is);
            } finally {
                is.close();
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Failed to parse reference definition file [HtmlCharacterEntityReferences.properties]: " + ex.getMessage());
        }
        // Parse reference definition properites.
        Enumeration<?> keys = entityReferences.propertyNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            int referredChar = Integer.parseInt(key);
            String reference = entityReferences.getProperty(key);
            ENTITY_REF_CHARACTER_MAP.put(reference, (char) referredChar);
        }
    }

    public static String htmlUnEscape(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        int last = 0, start = 0;
        Matcher m = ENTITY_REF_PATTERN.matcher(input);
        while (m.find(last)) {
            start = m.start();
            sb.append(input.substring(last, start));
            Character c = getUnescapeChar(m.group());
            if (c != null) {
                sb.append(c);
            } else {
                sb.append(m.group());
            }
            last = m.end();
        }
        sb.append(input.substring(last));

        return sb.toString();
    }

    public static Character getUnescapeChar(String entityRef) {
        try {
            Character c = null;
            if (entityRef.startsWith(HEX_REFERENCE_START)) {
                String charHexString = entityRef.substring(3, entityRef.length() - 1);
                c = ((char) Integer.parseInt(charHexString, 16));
            } else if (entityRef.startsWith(DECIMAL_REFERENCE_START)) {
                String charDecString = entityRef.substring(2, entityRef.length() - 1);
                c = ((char) Integer.parseInt(charDecString));
            } else {
                c = ENTITY_REF_CHARACTER_MAP.get(entityRef.substring(1, entityRef.length() - 1));
            }
            return c;
        } catch (Exception e) {
            return null;
        }
    }
}
