package org.pokesplash.gts.Listing;

import com.google.gson.Gson;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Manages all types of listings. Data is saved to memory here.
 */
public class ListingsProvider {
	// All active pokemon listings.
	private HashMap<UUID, PokemonListing> pokemonListings;
	// All active item listings.
	private HashMap<UUID, ItemListing> itemListings;

	/**
	 * Constructor to create a new list for both hashmaps.
	 */
	public ListingsProvider() {
		pokemonListings = new HashMap<>();
		itemListings = new HashMap<>();
	}

	/**
	 * Method to get all pokemon listings, as a collection.
	 * @return Pokemon listings in a collection.
	 */
	public Collection<PokemonListing> getPokemonListings() {
		return pokemonListings.values();
	}

	/**
	 * Method to get all pokemon listings that have been listed by a specific UUID.
	 * @param uuid the uuid to get the pokemon listings for.
	 * @return Arraylist of pokemon listings from the specified player.
	 */
	public ArrayList<PokemonListing> getPokemonListingsByPlayer(UUID uuid) {
		ArrayList<PokemonListing> playerListings = new ArrayList<>();

		for (PokemonListing pokemonListing : pokemonListings.values()) {
			if (pokemonListing.getSellerUuid().equals(uuid)) {
				playerListings.add(pokemonListing);
			}
		}
		return playerListings;
	}

	/**
	 * Method to add a new pokemon listing to the hashmap.
	 * @param listing the listing to add.
	 * @return true if the listing was successfully added.
	 * @throws IllegalArgumentException If the listing already exists.
	 */
	public boolean addPokemonListing(PokemonListing listing) throws IllegalArgumentException {
		if (pokemonListings.containsKey(listing.getId())) {
			throw new IllegalArgumentException("This listing already exists!");
		}
		pokemonListings.put(listing.getId(), listing);
		return writeToFile();
	}

	/**
	 * Method to remove a pokemon listing (expired or bought)
	 * @param listingId The listing id that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	public boolean removePokemonListing(UUID listingId) throws IllegalArgumentException {
		if (!pokemonListings.containsKey(listingId)) {
			throw new IllegalArgumentException("No listing with the UUID " + listingId + " exists.");
		}

		pokemonListings.remove(listingId);
		return writeToFile();
	}

	/**
	 * Method to get all item listings, as a collection.
	 * @return item listings in a collection.
	 */
	public Collection<ItemListing> getItemListings() {
		return itemListings.values();
	}

	/**
	 * Method to get all item listings that have been listed by a specific UUID.
	 * @param uuid the uuid to get the item listings for.
	 * @return Arraylist of item listings from the specified player.
	 */
	public ArrayList<ItemListing> getItemListingsByPlayer(UUID uuid) {
		ArrayList<ItemListing> playerListings = new ArrayList<>();

		for (ItemListing item : itemListings.values()) {
			if (item.getSellerUuid().equals(uuid)) {
				playerListings.add(item);
			}
		}
		return playerListings;
	}

	/**
	 * Method to add a new item listing to the hashmap.
	 * @param listing the listing to add.
	 * @return true if the listing was successfully added.
	 * @throws IllegalArgumentException If the listing already exists.
	 */
	public boolean addItemListing(ItemListing listing) throws IllegalArgumentException {
		if (itemListings.containsKey(listing.getId())) {
			throw new IllegalArgumentException("This listing already exists!");
		}
		itemListings.put(listing.getId(), listing);
		return writeToFile();
	}

	/**
	 * Method to remove an item listing (expired or bought)
	 * @param listingId The listing id that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	public boolean removeItemListing(UUID listingId) throws IllegalArgumentException {
		if (!itemListings.containsKey(listingId)) {
			throw new IllegalArgumentException("No listing with the UUID " + listingId + " exists.");
		}

		itemListings.remove(listingId);
		return writeToFile();
	}

	/**
	 * Method to write this object to the file.
	 * @return true if the file was successfully written.
	 */
	private boolean writeToFile() {

		Gson gson = Utils.newGson();

		System.out.println("before gson");

		String data = gson.toJson(this);

		System.out.println(data);

		return Utils.writeFileAsync("/config/gts/", "listings.json", data);
	}
}
