package org.pokesplash.gts.config.updaters.lang;

import com.google.gson.JsonObject;
import org.pokesplash.gts.config.Lang;
import org.pokesplash.gts.util.JsonFileUpdater;
import org.pokesplash.gts.util.Utils;

/**
 * Fills in whatever the file is missing, taking the values from a fresh
 * Lang.
 *
 * Gson does not run the constructor, so a key that is absent from the file
 * deserialises to null rather than to its default. Copying the defaults across
 * here is what keeps an older file working after new options are added, and it
 * needs no maintenance when more are added later.
 */
public class V3to4 implements JsonFileUpdater {
    @Override
    public int from() {
        return 3;
    }

    @Override
    public int to() {
        return 4;
    }

    @Override
    public JsonObject update(JsonObject in) {
        JsonObject out = in.deepCopy();
        JsonObject defaults = Utils.newGson().toJsonTree(new Lang()).getAsJsonObject();

        for (String key : defaults.keySet()) {
            if (!out.has(key)) {
                out.add(key, defaults.get(key));
            }
        }

        out.addProperty("version", 4);
        return out;
    }

    @Override
    public boolean validate(JsonObject json) {
        return json.has("version") && json.get("version").getAsInt() == 4;
    }
}
