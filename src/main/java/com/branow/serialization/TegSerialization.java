package com.branow.serialization;

import com.branow.serialization.parsing.Parser;

import java.io.File;

public class TegSerialization<T> implements Serialization<T> {



    private final FileText text;
    private final Parser<T> parser;

    public TegSerialization(File file) {
        this(new FileText(file));
    }

    public TegSerialization(String path) {
        this(new FileText(path));
    }

    public TegSerialization(FileText text) {
        this.text = text;
        parser = new Parser<>();
    }

    @Override
    public void serializate(T object) throws SerializationException {
        try {
            text.write(parser.parse(object));
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public T deserializate() throws SerializationException {
        try {
            return parser.parse(text.read());
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }



}
