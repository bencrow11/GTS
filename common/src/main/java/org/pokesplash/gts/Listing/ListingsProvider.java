package org.pokesplash.gts.Listing;

import com.google.gson.Gson;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Manages all types of listings. Data is saved to memory here.
 */
public class ListingsProvider {
	// All active pokemon listings.
	private List<PokemonListing> pokemonListings;
	// All active item listings.
	private List<ItemListing> itemListings;

	/**
	 * Constructor to create a new list for both hashmaps.
	 */
	public ListingsProvider() {
		pokemonListings = new ArrayList<>();
		itemListings = new ArrayList<>();
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
	 * Method to add a new pokemon listing to the hashmap.
	 * @param listing the listing to add.
	 * @return true if the listing was successfully added.
	 * @throws IllegalArgumentException If the listing already exists.
	 */
	public boolean addPokemonListing(PokemonListing listing) throws IllegalArgumentException {

		if (hasPokemonListing(listing.getId(), pokemonListings)) {
			throw new IllegalArgumentException("This listing already exists!");
		}
		pokemonListings.add(listing);
		return writeToFile();
	}

	/**
	 * Method to remove a pokemon listing (expired or bought)
	 * @param listing The listing that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	public boolean removePokemonListing(PokemonListing listing) throws IllegalArgumentException {
		if (hasPokemonListing(listing.getId(), pokemonListings)) {
			throw new IllegalArgumentException("No listing with the UUID " + listing.getId() + " exists.");
		}

		pokemonListings.remove(listing);
		return writeToFile();
	}

	/**
	 * Method to get all item listings, as a collection.
	 * @return item listings in a collection.
	 */
	public List<ItemListing> getItemListings() {
		return itemListings;
	}

	/**
	 * Method to get all item listings that have been listed by a specific UUID.
	 * @param uuid the uuid to get the item listings for.
	 * @return Arraylist of item listings from the specified player.
	 */
	public List<ItemListing> getItemListingsByPlayer(UUID uuid) {
		ArrayList<ItemListing> playerListings = new ArrayList<>();

		for (ItemListing item : itemListings) {
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
		if (hasItemListing(listing.getId(), itemListings)) {
			throw new IllegalArgumentException("This listing already exists!");
		}
		itemListings.add(listing);
		return writeToFile();
	}

	/**
	 * Method to remove an item listing (expired or bought)
	 * @param listing The listing that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	public boolean removeItemListing(ItemListing listing) throws IllegalArgumentException {
		if (hasItemListing(listing.getId(), itemListings)) {
			throw new IllegalArgumentException("No listing with the UUID " + listing.getId() + " exists.");
		}

		itemListings.remove(listing);
		return writeToFile();
	}

	private boolean hasPokemonListing(UUID listing, List<PokemonListing> pokemonListings) {
		for (PokemonListing pkm : pokemonListings) {
			if (pkm.getId().equals(listing)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasItemListing(UUID listing, List<ItemListing> itemListings) {
		for (ItemListing item : itemListings) {
			if (item.getId().equals(listing)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to write this object to the file.
	 * @return true if the file was successfully written.
	 */
	private boolean writeToFile() {

		Gson gson = Utils.newGson();
		String data = gson.toJson(this);

		CompletableFuture<Boolean> future = Utils.writeFileAsync("/config/gts/", "listings.json", data);

		return future.join();
	}

	public boolean initialize() {
		CompletableFuture<Boolean> future = Utils.readFileAsync("/config/gts/", "listings.json", el -> {
			Gson gson = Utils.newGson();
			ListingsProvider data = gson.fromJson(el, ListingsProvider.class);
			pokemonListings = data.getPokemonListings();
			itemListings = data.getItemListings();
		});

		return future.join();
	}
}
