package org.pokesplash.gts.util;

import com.google.gson.JsonObject;

public interface JsonFileUpdater {
    int from();
    int to();
    JsonObject update(JsonObject in);
    boolean validate(JsonObject json);
}
