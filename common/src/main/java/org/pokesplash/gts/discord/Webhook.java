package org.pokesplash.gts.discord;

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
import java.util.concurrent.CompletableFuture;

public abstract class Webhook {
    private static final long newColour = 4042994;
    private static final long soldColour = 3468102;

    private static final String pokemonBody = "{\n" +
            "    \"username\": \"gts\",\n" +
            "    \"embeds\": [\n" +
            "        {\n" +
            "            \"title\": \"%title%\",\n" +
            "            \"color\": %colour%,\n" +
            "            \"thumbnail\": {\n" +
            "                \"url\": \"https://img.pokemondb.net/sprites/home/%shiny%/%dex%%form%.png\"\n" +
            "            },\n" +
            "            \"fields\": [\n" +
            "                {\n" +
            "                    \"name\": \"\",\n" +
            "                    \"value\": \"**Pokemon:** %pokemon%\\n**Seller:** %seller%\\n**Price:** %price%\\n**Types:** %types%\\n**Nature:** %nature%**\\nAbility:** %ability%\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"IVs\",\n" +
            "                    \"value\": \"**HP:** %hp_iv%\\n**Atk:** %atk_iv%\\n**Def:** %def_iv%\\n**SpA:** %spa_iv%\\n**SpD:** %spd_iv%\\n**Spe:** %spe_iv%\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"EVs\",\n" +
            "                    \"value\": \"**HP:** %hp_ev%\\n**Atk:** %atk_ev%\\n**Def:** %def_ev%\\n**SpA:** %spa_ev%\\n**SpD:** %spd_ev%\\n**Spe:** %spe_ev%\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"Moves\",\n" +
            "                    \"value\": \"%moves%\"" +
            "                }\n" +
            "            ],\n" +
            "            \"author\": {\n" +
            "                \"name\": \"GTS\"\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private static final String itemBody = "{\n" +
            "    \"username\": \"gts\",\n" +
            "    \"embeds\": [\n" +
            "        {\n" +
            "            \"title\": \"%title%\",\n" +
            "            \"color\": %colour%,\n" +
            "            \"fields\": [\n" +
            "                {\n" +
            "                    \"name\": \"\",\n" +
            "                    \"value\": \"**Item:** %item%\\n**Seller:** %seller%\\n**Price:** %price%\\n**Amount:** %amount%\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"author\": {\n" +
            "                \"name\": \"GTS\"\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";



    public static void newListing(Listing listing) {

        if (listing.isPokemon()) {


            PokemonListing pokemonListing = (PokemonListing) listing;

            StringBuilder types = new StringBuilder();

            pokemonListing.getListing().getTypes().forEach(e -> {
                types.append(e.getDisplayName().getString()).append(" ");
            });

            StringBuilder moves = new StringBuilder();

            pokemonListing.getListing().getMoveSet().getMoves().forEach(e -> {
                moves.append(e.getDisplayName().getString()).append(", ");
            });
            moves.replace(moves.length() - 2, moves.length(), "");

            sendPost(pokemonBody
                    .replaceAll("%title%", "New Listing")
                    .replaceAll("%colour%", String.valueOf(newColour))
                    .replaceAll("%dex%", pokemonListing.getListing().getSpecies().getTranslatedName().getString().toLowerCase())
                    .replaceAll("%shiny%", pokemonListing.getListing().getShiny() ? "shiny" : "normal")
                    .replaceAll("%form%", FormParser.parseForm(pokemonListing.getListing().getForm().getName()))
                    .replaceAll("%pokemon%", pokemonListing.getListing().getSpecies().getTranslatedName().getString())
                    .replaceAll("%seller%", pokemonListing.getSellerName())
                    .replaceAll("%price%", pokemonListing.getPriceAsString())
                    .replaceAll("%types%", types.toString().trim())
                    .replaceAll("%nature%", Component.translatable(pokemonListing.getListing().getNature().getDisplayName()).getString())
                    .replaceAll("%ability%", Component.translatable(pokemonListing.getListing().getAbility().getDisplayName()).getString())
                    .replaceAll("%hp_iv%", pokemonListing.getListing().getIvs().get(Stats.HP) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.HP).toString())
                    .replaceAll("%atk_iv%", pokemonListing.getListing().getIvs().get(Stats.ATTACK) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.ATTACK).toString())
                    .replaceAll("%def_iv%", pokemonListing.getListing().getIvs().get(Stats.DEFENCE) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.DEFENCE).toString())
                    .replaceAll("%spa_iv%", pokemonListing.getListing().getIvs().get(Stats.SPECIAL_ATTACK) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.SPECIAL_ATTACK).toString())
                    .replaceAll("%spd_iv%", pokemonListing.getListing().getIvs().get(Stats.SPECIAL_DEFENCE) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.SPECIAL_DEFENCE).toString())
                    .replaceAll("%spe_iv%", pokemonListing.getListing().getIvs().get(Stats.SPEED) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.SPEED).toString())
                    .replaceAll("%hp_ev%", pokemonListing.getListing().getEvs().get(Stats.HP) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.HP).toString())
                    .replaceAll("%atk_ev%", pokemonListing.getListing().getEvs().get(Stats.ATTACK) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.ATTACK).toString())
                    .replaceAll("%def_ev%", pokemonListing.getListing().getEvs().get(Stats.DEFENCE) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.DEFENCE).toString())
                    .replaceAll("%spa_ev%", pokemonListing.getListing().getEvs().get(Stats.SPECIAL_ATTACK) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.SPECIAL_ATTACK).toString())
                    .replaceAll("%spd_ev%", pokemonListing.getListing().getEvs().get(Stats.SPECIAL_DEFENCE) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.SPECIAL_DEFENCE).toString())
                    .replaceAll("%spe_ev%", pokemonListing.getListing().getEvs().get(Stats.SPEED) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.SPEED).toString())
                    .replaceAll("%moves%", moves.toString().trim())
            );
        } else {

            ItemListing itemListing = (ItemListing) listing;

            sendPost(itemBody
                    .replaceAll("%title%", "New Listing")
                    .replaceAll("%colour%", String.valueOf(newColour))
                    .replaceAll("%item%", itemListing.getListing().getDisplayName().getString()
                            .replaceAll("\\[", "")
                            .replaceAll("\\]", ""))
                    .replaceAll("%seller%", itemListing.getSellerName())
                    .replaceAll("%price%", itemListing.getPriceAsString())
                    .replaceAll("%amount%", String.valueOf(itemListing.getListing().getCount()))
            );
        }
    }

    public static void soldListing(Listing listing) {

        if (listing.isPokemon()) {


            PokemonListing pokemonListing = (PokemonListing) listing;

            StringBuilder types = new StringBuilder();

            pokemonListing.getListing().getTypes().forEach(e -> {
                types.append(e.getDisplayName().getString()).append(" ");
            });

            StringBuilder moves = new StringBuilder();

            pokemonListing.getListing().getMoveSet().getMoves().forEach(e -> {
                moves.append(e.getDisplayName().getString()).append(", ");
            });
            moves.replace(moves.length() - 2, moves.length(), "");

            sendPost(pokemonBody
                    .replaceAll("%title%", "Sold Listing")
                    .replaceAll("%colour%", String.valueOf(soldColour))
                    .replaceAll("%dex%", pokemonListing.getListing().getSpecies().getTranslatedName().getString().toLowerCase())
                    .replaceAll("%shiny%", pokemonListing.getListing().getShiny() ? "shiny" : "normal")
                    .replaceAll("%form%", FormParser.parseForm(pokemonListing.getListing().getForm().getName()))
                    .replaceAll("%pokemon%", pokemonListing.getListing().getSpecies().getTranslatedName().getString())
                    .replaceAll("%seller%", pokemonListing.getSellerName())
                    .replaceAll("%price%", pokemonListing.getPriceAsString())
                    .replaceAll("%types%", types.toString().trim())
                    .replaceAll("%nature%", Component.translatable(pokemonListing.getListing().getNature().getDisplayName()).getString())
                    .replaceAll("%ability%", Component.translatable(pokemonListing.getListing().getAbility().getDisplayName()).getString())
                    .replaceAll("%hp_iv%", pokemonListing.getListing().getIvs().get(Stats.HP) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.HP).toString())
                    .replaceAll("%atk_iv%", pokemonListing.getListing().getIvs().get(Stats.ATTACK) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.ATTACK).toString())
                    .replaceAll("%def_iv%", pokemonListing.getListing().getIvs().get(Stats.DEFENCE) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.DEFENCE).toString())
                    .replaceAll("%spa_iv%", pokemonListing.getListing().getIvs().get(Stats.SPECIAL_ATTACK) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.SPECIAL_ATTACK).toString())
                    .replaceAll("%spd_iv%", pokemonListing.getListing().getIvs().get(Stats.SPECIAL_DEFENCE) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.SPECIAL_DEFENCE).toString())
                    .replaceAll("%spe_iv%", pokemonListing.getListing().getIvs().get(Stats.SPEED) == null ? "0" :
                            pokemonListing.getListing().getIvs().get(Stats.SPEED).toString())
                    .replaceAll("%hp_ev%", pokemonListing.getListing().getEvs().get(Stats.HP) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.HP).toString())
                    .replaceAll("%atk_ev%", pokemonListing.getListing().getEvs().get(Stats.ATTACK) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.ATTACK).toString())
                    .replaceAll("%def_ev%", pokemonListing.getListing().getEvs().get(Stats.DEFENCE) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.DEFENCE).toString())
                    .replaceAll("%spa_ev%", pokemonListing.getListing().getEvs().get(Stats.SPECIAL_ATTACK) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.SPECIAL_ATTACK).toString())
                    .replaceAll("%spd_ev%", pokemonListing.getListing().getEvs().get(Stats.SPECIAL_DEFENCE) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.SPECIAL_DEFENCE).toString())
                    .replaceAll("%spe_ev%", pokemonListing.getListing().getEvs().get(Stats.SPEED) == null ? "0" :
                            pokemonListing.getListing().getEvs().get(Stats.SPEED).toString())
                    .replaceAll("%moves%", moves.toString().trim())
            );
        } else {

            ItemListing itemListing = (ItemListing) listing;

            sendPost(itemBody
                    .replaceAll("%title%", "Sold ListingNew Listing")
                    .replaceAll("%colour%", String.valueOf(soldColour))
                    .replaceAll("%item%", itemListing.getListing().getDisplayName().getString()
                            .replaceAll("\\[", "")
                            .replaceAll("\\]", ""))
                    .replaceAll("%seller%", itemListing.getSellerName())
                    .replaceAll("%price%", itemListing.getPriceAsString())
                    .replaceAll("%amount%", String.valueOf(itemListing.getListing().getCount()))
            );
        }
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
