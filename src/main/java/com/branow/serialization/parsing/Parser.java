package com.branow.serialization.parsing;

public class Parser<T> {

    public String parse(T object) throws ParsingException {
        try {
            return new Coder(object).code();
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public T parse(String xml) throws ParsingException  {
        try {
            Object object = new Decoder(xml).decode();
            if (object == null) throw new NullPointerException();
            return (T) object;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    static class ParsingException extends Exception {

        public ParsingException() {
            super();
        }

        public ParsingException(String message) {
            super(message);
        }

        public ParsingException(String message, Throwable cause) {
            super(message, cause);
        }

        public ParsingException(Throwable cause) {
            super(cause);
        }
    }

}
