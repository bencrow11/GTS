package org.pokesplash.gts.discord;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import net.minecraft.network.chat.Component;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public abstract class Webhook {

    /**
     * Announces a listing that was just added.
     * @param listing the new listing.
     */
    public static void newListing(Listing listing) {
        org.pokesplash.gts.config.options.Webhook config = Gts.config.getDiscord();
        send(listing, config.getNewListingTitle(), config.getNewListingColour());
    }

    /**
     * Announces a listing that was just bought.
     * @param listing the sold listing.
     */
    public static void soldListing(Listing listing) {
        org.pokesplash.gts.config.options.Webhook config = Gts.config.getDiscord();
        send(listing, config.getSoldListingTitle(), config.getSoldListingColour());
    }

    /**
     * Fills the configured embed for this listing and posts it.
     *
     * Both events produce the same embed with a different title and colour,
     * which is why they share this method rather than repeating the field list.
     *
     * @param listing the listing to announce.
     * @param title   the embed title.
     * @param colour  the embed colour, in the decimal form Discord expects.
     */
    private static void send(Listing listing, String title, long colour) {
        org.pokesplash.gts.config.options.Webhook config = Gts.config.getDiscord();

        // replace and not replaceAll: these are literal substitutions, and a
        // value carrying a $ or a \ would otherwise be read as a group reference.
        String body = (listing.isPokemon() ? config.getPokemonBody() : config.getItemBody())
                .replace("%username%", config.getUsername())
                .replace("%author%", config.getAuthorName())
                .replace("%title%", title)
                .replace("%colour%", String.valueOf(colour))
                .replace("%seller%", listing.getSellerName())
                .replace("%price%", listing.getPriceAsString());

        if (listing.isPokemon()) {
            PokemonListing pokemonListing = (PokemonListing) listing;

            StringBuilder types = new StringBuilder();
            pokemonListing.getListing().getTypes().forEach(e -> types.append(e.getDisplayName().getString()).append(" "));

            StringBuilder moves = new StringBuilder();
            pokemonListing.getListing().getMoveSet().getMoves()
                    .forEach(e -> moves.append(e.getDisplayName().getString()).append(", "));
            if (moves.length() >= 2) {
                moves.replace(moves.length() - 2, moves.length(), "");
            }

            String sprite = config.getPokemonSpriteUrl()
                    .replace("%dex%", pokemonListing.getListing().getSpecies().getTranslatedName()
                            .getString().toLowerCase(Locale.ROOT))
                    .replace("%shiny%", pokemonListing.getListing().getShiny() ? "shiny" : "normal")
                    .replace("%form%", FormParser.parseForm(pokemonListing.getListing().getForm().getName()));

            body = body
                    .replace("%sprite%", sprite)
                    .replace("%pokemon%", pokemonListing.getListing().getSpecies().getTranslatedName().getString())
                    .replace("%types%", types.toString().trim())
                    .replace("%nature%", Component.translatable(
                            pokemonListing.getListing().getNature().getDisplayName()).getString())
                    .replace("%ability%", Component.translatable(
                            pokemonListing.getListing().getAbility().getDisplayName()).getString())
                    .replace("%moves%", moves.toString().trim())
                    .replace("%hp_iv%", iv(pokemonListing, Stats.HP))
                    .replace("%atk_iv%", iv(pokemonListing, Stats.ATTACK))
                    .replace("%def_iv%", iv(pokemonListing, Stats.DEFENCE))
                    .replace("%spa_iv%", iv(pokemonListing, Stats.SPECIAL_ATTACK))
                    .replace("%spd_iv%", iv(pokemonListing, Stats.SPECIAL_DEFENCE))
                    .replace("%spe_iv%", iv(pokemonListing, Stats.SPEED))
                    .replace("%hp_ev%", ev(pokemonListing, Stats.HP))
                    .replace("%atk_ev%", ev(pokemonListing, Stats.ATTACK))
                    .replace("%def_ev%", ev(pokemonListing, Stats.DEFENCE))
                    .replace("%spa_ev%", ev(pokemonListing, Stats.SPECIAL_ATTACK))
                    .replace("%spd_ev%", ev(pokemonListing, Stats.SPECIAL_DEFENCE))
                    .replace("%spe_ev%", ev(pokemonListing, Stats.SPEED));
        } else {
            ItemListing itemListing = (ItemListing) listing;

            body = body
                    .replace("%item%", itemListing.getListing().getDisplayName().getString()
                            .replace("[", "")
                            .replace("]", ""))
                    .replace("%amount%", String.valueOf(itemListing.getListing().getCount()));
        }

        sendPost(body);
    }

    /**
     * Reads an IV, treating an absent one as zero.
     * @param listing the listing the Pokemon belongs to.
     * @param stat    the stat to read.
     * @return the value as a String.
     */
    private static String iv(PokemonListing listing, Stat stat) {
        Integer value = listing.getListing().getIvs().get(stat);
        return value == null ? "0" : value.toString();
    }

    /**
     * Reads an EV, treating an absent one as zero.
     * @param listing the listing the Pokemon belongs to.
     * @param stat    the stat to read.
     * @return the value as a String.
     */
    private static String ev(PokemonListing listing, Stat stat) {
        Integer value = listing.getListing().getEvs().get(stat);
        return value == null ? "0" : value.toString();
    }

    private static void sendPost(String body) {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Gts.config.getDiscord().getUrl()))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<String>> response =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> responseBody = response.join();

        if (responseBody.statusCode() != 204) {
            Gts.LOGGER.error("Could not send discord webhook. Error: " + responseBody.body());
        }
    }
}
