package org.pokesplash.gts.history;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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

    @Override
    public boolean isHistoryItemValid() {
        return pokemon != null && pokemon.isJsonObject();
    }

    @Override
    public MutableComponent getDisplayName() {
        Style base = Style.EMPTY.withItalic(false);
        Style blue = base.withColor(TextColor.parseColor("blue").getOrThrow());
        Style dark_aqua = base.withColor(TextColor.parseColor("dark_aqua").getOrThrow());
        Style red = base.withColor(TextColor.parseColor("red").getOrThrow());
        Style yellow = base.withColor(TextColor.parseColor("yellow").getOrThrow());
        Style white = base.withColor(TextColor.parseColor("white").getOrThrow());
        Pokemon pokemon = this.getListing();
        boolean isShiny = pokemon.getShiny();
        MutableComponent displayName = pokemon.getDisplayName().setStyle(isShiny ? yellow : dark_aqua);
        if (isShiny) {
            displayName.append(Component.literal("★").setStyle(red));
        }
        displayName.append(" ").append(Component.translatable("cobblemon.ui.lv.number", pokemon.getLevel()).setStyle(white));
        switch (pokemon.getGender().toString()) {
            case "MALE":
                displayName.append(Component.literal(" ♂").setStyle(blue));
                break;
            case "FEMALE":
                displayName.append(Component.literal(" ♀").setStyle(red));
                break;
            default:
                break;
        }
        return displayName;
    }
}
