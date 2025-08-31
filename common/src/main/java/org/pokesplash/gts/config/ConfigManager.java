package org.pokesplash.gts.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.config.updaters.config.V2to3;
import org.pokesplash.gts.util.JsonFileUpdater;
import org.pokesplash.gts.util.Utils;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigManager {

    private Gson gson = Utils.newGson();

    public Config loadConfig() {

        AtomicReference<Config> config = new AtomicReference<>(new Config());
        AtomicReference<Exception> failure = new AtomicReference<>();
        AtomicReference<Boolean> requiresWrite = new AtomicReference<>(false);

        CompletableFuture<Boolean> read = Utils.readFileAsync("/config/gts/", "config.json",
                data -> {

                    JsonObject json = JsonParser.parseString(data).getAsJsonObject();

                    int version;

                    try {
                        version = json.get("version").getAsInt();
                    } catch (Exception e) {
                        version = 2;
                        json.addProperty("version", version);
                    }

                    if (!json.has("version") ||  version < 3 ) {
                         try {
                             json = updateConfig(json);
                             requiresWrite.set(true);
                         } catch (Exception e) {
                             failure.set(e);
                         }
                    }

                    try {
                        config.set(gson.fromJson(json, Config.class));
                    } catch (Exception e) {
                        failure.set(new Exception("Failed to load config"));
                    }


        });

        if (!read.join()) {
            Gts.LOGGER.info("Could not find config for GTS. Generating a new one.");
            requiresWrite.set(true);
        } else if (failure.get() != null) {
            Gts.LOGGER.error("Could not update GTS Config: " +  failure.get().getMessage());
        }

        if  (requiresWrite.get()) {
            config.get().write();
        }

        return config.get();
    }

    private JsonObject updateConfig(JsonObject json) {

        HashMap<Integer, JsonFileUpdater> versions = new HashMap<>();
        versions.put(2, new V2to3());

        JsonObject out = json;
        int currentVersion = out.get("version").getAsInt();

        while (currentVersion < Gts.CONFIG_FILE_VERSION) {
            JsonFileUpdater updater = versions.get(currentVersion);

            out = updater.update(out);

            if (!updater.validate(out)) {
                throw  new RuntimeException("Config is invalid.");
            }

            currentVersion =  out.get("version").getAsInt();
        }

        return out;
    }
}
