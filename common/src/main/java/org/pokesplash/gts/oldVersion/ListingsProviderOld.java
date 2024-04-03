package org.pokesplash.gts.oldVersion;

import com.google.gson.Gson;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.util.Utils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Manages all types of listings. Data is saved to memory here.
 */
public class ListingsProviderOld {
    // All active pokemon listings.
    private ArrayList<PokemonListing> pokemonListings;
    // All active item listings.
    private ArrayList<ItemListing> itemListings;
    // Where listings are stored once they have expired.
    private HashMap<UUID, ArrayList<PokemonListing>> expiredPokemonListings;
    private HashMap<UUID, ArrayList<ItemListing>> expiredItemListings;

    /**
     * Constructor to create a new list for both hashmaps.
     */
    public ListingsProviderOld() {
        pokemonListings = new ArrayList<>();
        itemListings = new ArrayList<>();
        expiredPokemonListings = new HashMap<>();
        expiredItemListings = new HashMap<>();
    }

    /**
     * Method to get all pokemon listings, as a collection.
     * @return Pokemon listings in a collection.
     */
    public List<PokemonListing> getPokemonListings() {
        return pokemonListings;
    }

    /**
     * Method to get all pokemon listings that have been listed by a specific UUID.
     * @param uuid the uuid to get the pokemon listings for.
     * @return Arraylist of pokemon listings from the specified player.
     */
    public List<PokemonListing> getPokemonListingsByPlayer(UUID uuid) {
        ArrayList<PokemonListing> playerListings = new ArrayList<>();

        for (PokemonListing pokemonListing : pokemonListings) {
            if (pokemonListing.getSellerUuid().equals(uuid)) {
                playerListings.add(pokemonListing);
            }
        }
        return playerListings;
    }

    /**
     * Method to get all item listings, as a collection.
     * @return item listings in a collection.
     */
    public List<ItemListing> getItemListings() {
        return itemListings;
    }

    /**
     * Method to check if a pokemon listing already exists.
     * @param listing the listing to check for
     * @param pokemonListings the list to check in
     * @return true if the listing already exists in the list.
     */
    private boolean hasPokemonListing(UUID listing, List<PokemonListing> pokemonListings) {
        for (PokemonListing pkm : pokemonListings) {
            if (pkm.getId().equals(listing)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check if a pokemon listing already exists.
     * @param listing the listing to check for
     * @param itemListings the list to check in
     * @return true if the listing already exists in the list.
     */
    private boolean hasItemListing(UUID listing, List<ItemListing> itemListings) {
        for (ItemListing item : itemListings) {
            if (item.getId().equals(listing)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks that a player has some expired pokemon listings.
     * @param playerUUID The player to check for expired listings
     * @return true if the player has expired listings.
     */
    public boolean hasExpiredPokemonListings(UUID playerUUID) {
        return expiredPokemonListings.containsKey(playerUUID);
    }

    /**
     * Checks that a player has some expired item listings.
     * @param playerUUID The player to check for expired listings
     * @return true if the player has expired listings.
     */
    public boolean hasExpiredItemListings(UUID playerUUID) {
        return expiredItemListings.containsKey(playerUUID);
    }


    public List<PokemonListing> getExpiredPokemonListings(UUID playerId) {
        return expiredPokemonListings.get(playerId);
    }

    public HashMap<UUID, ArrayList<PokemonListing>> getAllExpiredPokemonListings() {
        return expiredPokemonListings;
    }

    public HashMap<UUID, ArrayList<ItemListing>> getAllExpiredItemListings() {
        return expiredItemListings;
    }

    public List<ItemListing> getExpiredItemListings(UUID playerId) {
        return expiredItemListings.get(playerId);
    }

}