package com.branow.serialization.parsing;

public class Property {

    public static Property propertyValue(String value) {
        return new Property("value", value);
    }

    public static Property propertyName(String name) {
        return new Property("name", name);
    }

    public static Property propertyClass(String value) {
        return new Property("class", value);
    }

    private String name;
    private String value;

    public Property(String name, String value) throws IllegalArgumentException {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=\"" + value + "\"";
    }

}
