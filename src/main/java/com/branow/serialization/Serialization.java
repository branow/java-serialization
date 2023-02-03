package com.branow.serialization;

import java.io.File;
import java.nio.file.Path;

public interface Serialization<T> {

    static<T> Serialization<T> serializationXML(String name) {
        return new TegSerialization<>(new FileText(name + ".xml"));
    }

    static<T> Serialization<T> serializationXML(String path, String name) {
        return new TegSerialization<>(new FileText(path + File.separator + name + ".xml"));
    }

    void serializate(T object) throws SerializationException;

    T deserializate() throws SerializationException;

}
