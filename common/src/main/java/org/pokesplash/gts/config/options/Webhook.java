package org.pokesplash.gts.config.options;

/**
 * Settings for the Discord webhook: whether to send it, where to, and what it
 * looks like.
 *
 * Everything below the url is presentation. The defaults reproduce the embed
 * GTS has always sent, so an existing config keeps behaving the same, but a
 * server that wants its own wording, colours or layout can change it here
 * instead of needing a fork.
 */
public class Webhook {
    private boolean useWebhooks;
    private String url;

    // Shown as the sender of the message and as the embed author.
    private String username;
    private String authorName;

    // Titles and side colours, one pair per event. Colours are the decimal form
    // Discord expects, not hex: 4042994 is #3DB472.
    private String newListingTitle;
    private long newListingColour;
    private String soldListingTitle;
    private long soldListingColour;

    // Where the Pokemon thumbnail comes from. Takes %dex%, %shiny% and %form%.
    private String pokemonSpriteUrl;

    // The raw embed bodies. Anything Discord accepts goes here; GTS only fills
    // in the placeholders below and posts the result as-is.
    //
    //   both:    %username% %author% %title% %colour% %seller% %price%
    //   pokemon: %sprite% %pokemon% %types% %nature% %ability% %moves%
    //            %hp_iv% %atk_iv% %def_iv% %spa_iv% %spd_iv% %spe_iv%
    //            %hp_ev% %atk_ev% %def_ev% %spa_ev% %spd_ev% %spe_ev%
    //   item:    %item% %amount%
    private String pokemonBody;
    private String itemBody;

    public Webhook() {
        useWebhooks = false;
        url = "WEBHOOK_URL";
        username = "GTS";
        authorName = "GTS";
        newListingTitle = "New Listing";
        newListingColour = 4042994;
        soldListingTitle = "Sold Listing";
        soldListingColour = 3468102;
        pokemonSpriteUrl = "https://img.pokemondb.net/sprites/home/%shiny%/%dex%%form%.png";

        pokemonBody = "{\n" +
                "    \"username\": \"%username%\",\n" +
                "    \"embeds\": [\n" +
                "        {\n" +
                "            \"title\": \"%title%\",\n" +
                "            \"color\": %colour%,\n" +
                "            \"thumbnail\": {\n" +
                "                \"url\": \"%sprite%\"\n" +
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
                "                \"name\": \"%author%\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        itemBody = "{\n" +
                "    \"username\": \"%username%\",\n" +
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
                "                \"name\": \"%author%\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    public boolean isUseWebhooks() {
        return useWebhooks;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getNewListingTitle() {
        return newListingTitle;
    }

    public long getNewListingColour() {
        return newListingColour;
    }

    public String getSoldListingTitle() {
        return soldListingTitle;
    }

    public long getSoldListingColour() {
        return soldListingColour;
    }

    public String getPokemonSpriteUrl() {
        return pokemonSpriteUrl;
    }

    public String getPokemonBody() {
        return pokemonBody;
    }

    public String getItemBody() {
        return itemBody;
    }
}
