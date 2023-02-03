package com.branow.serialization.parsing;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Teg {

    public static final String ARRAY_CLASS_SEPARATOR = ":";
    public static final String TEG_ARRAY = "array", TEG_OBJECT = "object";
    private static final String ROW_END = "\u2A3B";

    public static Teg primitiveTeg(Object object, String name, PrimitiveType type) {
        if (object instanceof String)
            object = toValueString((String) object);
        return new Teg(type.getValue(), Property.propertyName(name), Property.propertyValue(object.toString()));
    }

    public static Teg objectTeg(String name, String clas) {
        return new Teg(TEG_OBJECT, Property.propertyName(name), Property.propertyClass(clas));
    }

    public static Teg arrayTeg(String name, String clas) {
        return new Teg(TEG_ARRAY, Property.propertyName(name), Property.propertyClass(clas));
    }

    public static String toValueString(String value) {
        return value.replaceAll("\n", ROW_END);
    }

    public static String fromValueString(String value) {
        return value.replaceAll(ROW_END, "\n");
    }

    private String name;
    private Property[] properties;

    public Teg(String name, Property... properties) {
        this.name = name;
        this.properties = properties;
    }

    public String head(boolean inLine) {
        String prop = Arrays.stream(properties)
                .map(Property::toString)
                .collect(Collectors.joining(" "));
        if (!prop.isEmpty()) prop = " " + prop;
        if (inLine)
            return "<" + name + prop + "/>";
        else
            return "<" + name + prop + ">";

    }

    public String head() {
        return head(false);
    }

    public String tail() {
        return "</" + name + ">";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Property[] getProperties() {
        return properties;
    }

    public void setProperties(Property[] properties) {
        this.properties = properties;
    }
}

