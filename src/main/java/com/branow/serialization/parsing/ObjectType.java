package com.branow.serialization.parsing;

public class ObjectType extends Map<String, Object> {

    static final int PRIMITIVE = 0, ARRAY = 1, OBJECT = 2;

    public static ObjectType getPrimitive(String name, Object value) {
        return new ObjectType(name, value, PRIMITIVE);
    }

    public static ObjectType getArray(String name, Object value, int deep) {
        ObjectType array = new ObjectType(name, value, ARRAY);
        array.setDepth(deep);
        return array;
    }

    public static ObjectType getObject(String name, Object value) {
        return new ObjectType(name, value, OBJECT);
    }

    private int type;
    private int depth;

    public ObjectType(String key, Object value, int type) {
        super(key, value);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
