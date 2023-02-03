package com.branow.serialization.parsing;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Coder {

    private Object object;

    public Coder(Object object) {
        this.object = object;
    }

    public Object getCode() {
        return object;
    }

    public void setCode(Object object) {
        this.object = object;
    }

    public String code() throws IllegalAccessException {
        return code(object, "parent", new StringBuilder(), "");
    }

    private String code(Object object, String name, StringBuilder sb, String tab) throws IllegalAccessException {
        if (object == null) return "";
        PrimitiveType type = isPrimitive(object);
        if (type != null) {
            sb.append(tab).append(Teg.primitiveTeg(object, name, type).head(true)).append("\n");
        } else if (object.getClass().isArray()) {
            codeArray(object, name, sb, tab);
        } else {
            codeObject(object, name, sb, tab);
        }
        return sb.toString();
    }

    private void codeArray(Object object, String name, StringBuilder sb, String tab) throws IllegalAccessException {
        Teg teg = Teg.arrayTeg(name,  getComponentClassOfArray(object));
        sb.append(tab).append(teg.head()).append("\n");
        for (Object o: (Object[]) object) {
            code(o, "item", sb, tab + "\t");
        }
        sb.append(tab).append(teg.tail()).append("\n");
    }

    private void codeObject(Object object, String name, StringBuilder sb, String tab) throws IllegalAccessException {
        Teg teg = Teg.objectTeg(name, object.getClass().getName());
        sb.append(tab).append(teg.head()).append("\n");
        for (Field field: object.getClass().getFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            code(field.get(object), field.getName(), sb, tab + "\t");
        }
        sb.append(tab).append(teg.tail()).append("\n");
    }

    private PrimitiveType isPrimitive(Object object) {
        for (PrimitiveType type: PrimitiveType.values()) {
            if (object.getClass().getName().equals(type.getFullName()))
                return type;
        }
        return null;
    }

    private String getComponentClassOfArray(Object array) {
        StringBuilder sb = new StringBuilder();
        Class<? extends Object> cl = array.getClass();
        while (cl.isArray()) {
            sb.append(Teg.TEG_ARRAY).append(Teg.ARRAY_CLASS_SEPARATOR);
            cl = cl.getComponentType();
        }
        PrimitiveType type = PrimitiveType.getPrimitiveOfClassName(cl.getName());
        if (type != null)
            sb.append(type.getValue());
        else
            sb.append(cl.getName());
        return sb.toString();
    }


}
