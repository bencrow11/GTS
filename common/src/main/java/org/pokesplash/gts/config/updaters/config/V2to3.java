package org.pokesplash.gts.config.updaters.config;

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

        JsonArray array = new JsonArray();
        array.add("simpletms");

        out.add("removedModDescriptions", array);
        out.addProperty("version", 3);
        return out;
    }

    @Override
    public boolean validate(JsonObject json) {

        String[] properties = new String[]{"version", "enablePokemonSales", "enableItemSales", "enableAsyncSearches",
        "broadcastListings", "enablePermissionNodes", "maxListingsPerPlayer", "listingDuration", "discord", "showBreedable",
        "taxRate", "minPrice1IV", "minPrice2IV", "minPrice3IV", "minPrice4IV", "minPrice5IV", "minPrice6IV", "minPriceHA",
        "minPriceLegendary", "minPriceUltrabeast", "maximumPrice", "customItemPrices", "bannedItems", "customPokemonPrices",
        "bannedPokemon", "removedModDescriptions"};

        for (String property : properties) {
            if (!json.has(property)) {
                System.out.println(property);
                return false;
            }
        }

        return true;
    }
}
