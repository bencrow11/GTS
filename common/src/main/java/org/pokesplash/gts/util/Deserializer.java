package org.pokesplash.gts.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Generic class that allows Gson to deserialize interfaces.
 */
public class Deserializer implements JsonDeserializer {
    private Class<?> implClass;


    public Deserializer(Class<?> c) {
        implClass = c;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, implClass);
    }
}
