package org.pokesplash.gts.history;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import org.pokesplash.gts.Listing.PokemonListing;

/**
 * Class that is used to save sold Pokemon.
 */
public class PokemonHistoryItem extends HistoryItem<Pokemon> {

    private JsonElement pokemon;

    public PokemonHistoryItem(PokemonListing listing, String buyerName) {
        super(listing.isPokemon(), listing.getSellerUuid(), listing.getSellerName(), listing.getPrice(), buyerName);
        this.pokemon = listing.getListingAsJsonObject();
    }

    /**
     * Method to get the Pokemon that has been sold.
     * @return The Pokemon that has been sold.
     */
    @Override
    public Pokemon getListing() {
        return Pokemon.getCODEC().decode(JsonOps.INSTANCE, pokemon).getOrThrow().getFirst();
    }
}
