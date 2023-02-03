package com.branow.serialization.parsing;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Decoder {
    
    private String code;

    public Decoder(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object decode() throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        return decodeTeg(Arrays.stream(code.split("\n")).toList().iterator()).getValue();
    }

    private ObjectType decodeTeg(Iterator<String> iterator)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {

        String code = null;
        if (iterator.hasNext())
            code = iterator.next();
        else return null;

        ObjectType objectType = decodeTeg(code);
        return setValue(objectType, iterator);
    }

    private ObjectType setValue(ObjectType objectType, Iterator<String> iterator) throws NoSuchFieldException,
            ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (objectType == null) return null;

        if (objectType.getType() == ObjectType.ARRAY) {
            setValueArray(objectType, iterator);
        } else if (objectType.getType() == ObjectType.OBJECT) {
            setValueObject(objectType, iterator);
        }
        return objectType;
    }

    private void setValueObject(ObjectType objectType, Iterator<String> iterator) throws NoSuchFieldException,
            ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        ObjectType field = null;
        while ((field = decodeTeg(iterator)) != null) {
            objectType.getValue().getClass()
                    .getField(field.getKey())
                    .set(objectType.getValue(), field.getValue());
        }
    }

    private void setValueArray(ObjectType objectType, Iterator<String> iterator) throws NoSuchFieldException,
            ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<Object> list = new ArrayList<>();
        ObjectType field = null;
        while ((field = decodeTeg(iterator)) != null) {
            list.add(field.getValue());
        }
        objectType.setValue(cast(objectType.getValue().getClass(), objectType.getDepth(), list.toArray()));
    }

    private<T> T[] cast(Class<T> cast, int depth, Object[] o) {
        T[] t = (T[]) o.getClass().cast(Array.newInstance(cast, getArrayDimensions(depth, o)));
        for (int i=0; i<t.length; i++)
            t[i] = (T) o[i];
        return t;
    }

    private int[] getArrayDimensions(int depth, Object[] o) {
        int[] dimensions = new int[depth];
        Object array = o;
        for (int i=0; i<dimensions.length; i++) {
            dimensions[i] = ((Object[]) array).length;
            if (dimensions[i] > 0) {
                array = ((Object[]) array)[0];
            } else {
                array = new Object[0];
            }

        }
        return dimensions;
    }

    private ObjectType decodeTeg(String code)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (code.contains("</object>") || code.contains("</array>")) return null;
        Property[] properties = decodeProperties(code.substring(0, code.length()-1));
        String name = getName(properties).getValue();

        return decodeTeg(code, properties, name);
    }

    private Property[] decodeProperties(String code) {
        code = code.replaceFirst("[ \t\r\n]*<[A-z]+ ", "");
        code = code.replaceFirst("[/>]", "");
        String[] prop = splitTeg(code);
        Property[] properties = new Property[prop.length];
        for (int i=0; i<properties.length; i++) {
            properties[i] = decodeProperty(prop[i]);
        }
        return properties;
    }

    private String[] splitTeg(String code) {
        List<String> prop = new ArrayList<>();
        int last = 0;
        char[] chars = code.toCharArray();
        boolean inQuotation = false;
        for (int i=0; i<chars.length; i++) {
            if (chars[i] == '\"') {
                inQuotation = !inQuotation;
            } else if (chars[i] == ' ' && !inQuotation) {
                prop.add(code.substring(last, i));
                last = i+1;
            }
        }
        prop.add(code.substring(last));
        return prop.toArray(String[]::new);
    }

    private Property decodeProperty(String code) {
        String[] strings = code.split("=");
        if (strings.length < 2) return null;
        return new Property(strings[0], strings[1].replaceAll("\"", ""));
    }

    private ObjectType decodeTeg(String code, Property[] properties, String name) throws ClassNotFoundException, InvocationTargetException,
            InstantiationException, IllegalAccessException, NoSuchMethodException {
        PrimitiveType type = isPrimitive(code);
        if (type != null) {
            return decodePrimitiveTeg(type, properties, name);
        } else {
            String clas = getClass(properties).getValue();
            if (isArray(code))
                return decodeArrayTeg(clas, name);
            else
                return decodeObjectTeg(clas, name);
        }
    }

    private ObjectType decodePrimitiveTeg(PrimitiveType type, Property[] properties, String name)
            throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String value = getValue(properties).getValue();
        if (type == PrimitiveType.STRING) value = Teg.fromValueString(value);
        return ObjectType.getPrimitive(name, type.create(value));
    }

    private ObjectType decodeArrayTeg(String clas, String name) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        return  ObjectType.getArray(name, getArrayComponentObject(clas), getArrayDepth(clas));
    }

    private Object getArrayComponentObject(String clas) throws ClassNotFoundException, InvocationTargetException,
            InstantiationException, IllegalAccessException, NoSuchMethodException {

        String[] classes = clas.split(Teg.ARRAY_CLASS_SEPARATOR);
        String name = classes[classes.length - 1];
        PrimitiveType type = PrimitiveType.getPrimitiveOfValue(name);
        if (type != null)
            return type.create();
        return  Class.forName(name).getConstructor().newInstance();
    }

    private int getArrayDepth(String clas) {
        return clas.split(Teg.ARRAY_CLASS_SEPARATOR).length - 1;
    }

    private ObjectType decodeObjectTeg(String clas, String name) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        return ObjectType.getObject(name, Class.forName(clas).getConstructor().newInstance());
    }

    private Property getValue(Property[] properties) {
        return getPropertyType("value", properties);
    }

    private Property getClass(Property[] properties) {
        return getPropertyType("class", properties);
    }

    private Property getName(Property[] properties) {
        return getPropertyType("name", properties);
    }

    private Property getPropertyType(String type, Property[] properties) {
        return Arrays.stream(properties)
                .filter(e -> e.getName().equals(type))
                .findAny().orElse(null);
    }


    
    private PrimitiveType isPrimitive(String code) {
        for (PrimitiveType type: PrimitiveType.values()) {
            if (code.contains("<" + type.getValue()))
                return type;
        }
        return null;
    }

    private boolean isArray(String code) {
        return code.contains("<array");
    }
    
}
