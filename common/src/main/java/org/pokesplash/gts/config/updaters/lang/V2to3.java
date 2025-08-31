package org.pokesplash.gts.config.updaters.lang;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.pokesplash.gts.util.JsonFileUpdater;

public class V2to3 implements JsonFileUpdater {
    @Override
    public int from() {
        return 2;
    }

    @Override
    public int to() {
        return 3;
    }

    @Override
    public JsonObject update(JsonObject in) {
        JsonObject out = in.deepCopy();
        out.addProperty("version", 3);
        return out;
    }

    @Override
    public boolean validate(JsonObject json) {

        return true;
    }
}
