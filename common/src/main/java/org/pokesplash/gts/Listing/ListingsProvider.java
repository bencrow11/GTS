package org.pokesplash.gts.Listing;

import com.google.gson.Gson;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Manages all types of listings. Data is saved to memory here.
 */
public class ListingsProvider {
	// All active pokemon listings.
	@Deprecated
	private ArrayList<PokemonListing> pokemonListings;
	// All active item listings.
	@Deprecated
	private ArrayList<ItemListing> itemListings;
	// Where listings are stored once they have expired.
	@Deprecated
	private HashMap<UUID, ArrayList<PokemonListing>> expiredPokemonListings;
	@Deprecated
	private HashMap<UUID, ArrayList<ItemListing>> expiredItemListings;


	private ArrayList<Listing> listings; // Active listings.

	private HashMap<UUID, ArrayList<Listing>> expiredListings; // Expired listings.

	/**
	 * Constructor to create a new list for both hashmaps.
	 */
	public ListingsProvider() {
		pokemonListings = new ArrayList<>();	// TODO Remove
		itemListings = new ArrayList<>();	// TODO Remove
		expiredPokemonListings = new HashMap<>();	// TODO Remove
		expiredItemListings = new HashMap<>();	// TODO Remove

		listings = new ArrayList<>();
		expiredListings = new HashMap<>();
	}

	/**
	 * Method that returns all active listings.
	 * @return A list of active listings.
	 */
	public List<Listing> getListings() {
		return listings;
	}


	/**
	 * Method to get all pokemon listings, as a collection.
	 * @return Pokemon listings in a collection.
	 */
	public List<PokemonListing> getPokemonListings() {

		ArrayList<PokemonListing> pkmListings = new ArrayList<>();

		// Filters all listings and finds the listings that are Pokemon only.
		for (Listing listing : listings) {
			if (listing instanceof PokemonListing) {
				pkmListings.add((PokemonListing) listing);
			}
		}

		return pkmListings;
	}

	/**
	 * Method to get all pokemon listings that have been listed by a specific UUID.
	 * @param uuid the uuid to get the pokemon listings for.
	 * @return Arraylist of pokemon listings from the specified player.
	 */
	public List<PokemonListing> getPokemonListingsByPlayer(UUID uuid) {
		ArrayList<PokemonListing> playerListings = new ArrayList<>();

		for (PokemonListing pokemonListing : getPokemonListings()) {
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
		ArrayList<ItemListing> itemListings = new ArrayList<>();

		// Filters all listings and finds the listings that are Pokemon only.
		for (Listing listing : listings) {
			if (listing instanceof ItemListing) {
				itemListings.add((ItemListing) listing);
			}
		}

		return itemListings;
	}

	/**
	 * Method to get all item listings that have been listed by a specific UUID.
	 * @param uuid the uuid to get the item listings for.
	 * @return Arraylist of item listings from the specified player.
	 */
	public List<ItemListing> getItemListingsByPlayer(UUID uuid) {
		ArrayList<ItemListing> playerListings = new ArrayList<>();

		for (ItemListing item : getItemListings()) {
			if (item.getSellerUuid().equals(uuid)) {
				playerListings.add(item);
			}
		}
		return playerListings;
	}

	/**
	 * Method to add a new listing to the array.
	 * @param listing the listing to add.
	 * @return true if the listing was successfully added.
	 * @throws IllegalArgumentException If the listing already exists.
	 */
	public boolean addListing(Listing listing) throws IllegalArgumentException {

		if (hasListing(listing.getId(), listings)) {
			throw new IllegalArgumentException("This listing already exists!");
		}
		listings.add(listing);
		Gts.timers.addTimer(listing);
		return writeToFile();
	}

	/**
	 * Method to remove a listing (expired or bought)
	 * @param listing The listing that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	public boolean removeListing(Listing listing) throws IllegalArgumentException {
		if (!hasListing(listing.getId(), listings)) {
			throw new IllegalArgumentException("No listing with the UUID " + listing.getId() + " exists.");
		}

		listings.remove(listing);
		Gts.timers.deleteTimer(listing);
		return writeToFile();
	}

	/**
	 * Method to check if a pokemon listing already exists.
	 * @param id the listing to check for
	 * @param listings the list to check in
	 * @return true if the listing already exists in the list.
	 */
	private boolean hasListing(UUID id, List<Listing> listings) {
		for (Listing listing : listings) {
			if (listing.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks that a player has some expired listings.
	 * @param playerUUID The player to check for expired listings
	 * @return true if the player has expired listings.
	 */
	public boolean hasExpiredListings(UUID playerUUID) {
		return expiredListings.containsKey(playerUUID);
	}

	/**
	 * Method to add an expired listing to the list.
	 * @param listing The listing to add.
	 * @return true if successfully written to file.
	 */
	public boolean addExpiredListing(Listing listing) {
		if (expiredListings.containsKey(listing.getSellerUuid())) {
			ArrayList<Listing> currentListings = expiredListings.get(listing.getSellerUuid());
			if (!currentListings.contains(listing)) {
				currentListings.add(listing);
				expiredListings.put(listing.getSellerUuid(), currentListings);
			}
		} else {
			expiredListings.put(listing.getSellerUuid(), new ArrayList<>(List.of(listing)));
		}
		return writeToFile();
	}

	/**
	 * Method to remove an expired listing that has been collected by the player.
	 * @param listing The listing to remove.
	 * @return true if successfully written to file.
	 */
	public boolean removeExpiredListing(Listing listing) {

		if (expiredListings.get(listing.getSellerUuid()) == null) {
			return false;
		}

		ArrayList<Listing> listings = expiredListings.get(listing.getSellerUuid());
		if (listings.contains(listing)) {
			listings.remove(listing);
			expiredListings.put(listing.getSellerUuid(), listings);
		} else {
			return false;
		}
		return writeToFile();
	}

	public Listing getListingById(UUID id) {
		for (Listing listing : listings) {
			if (listing.getId().equals(id)) {
				return listing;
			}
		}

		return null;
	}

	public List<Listing> getExpiredListingsOfPlayer(UUID player) {
		return expiredListings.get(player);
	}

	public HashMap<UUID, ArrayList<Listing>> getExpiredListings() {
		return expiredListings;
	}












	/**
	 * Method to add a new pokemon listing to the hashmap.
	 * @param listing the listing to add.
	 * @return true if the listing was successfully added.
	 * @throws IllegalArgumentException If the listing already exists.
	 */
	@Deprecated
	public boolean addPokemonListing(PokemonListing listing) throws IllegalArgumentException {

		if (hasPokemonListing(listing.getId(), getPokemonListings())) {
			throw new IllegalArgumentException("This listing already exists!");
		}
		listings.add(listing);
		Gts.timers.addTimer(listing);
		return writeToFile();
	}

	/**
	 * Method to remove a pokemon listing (expired or bought)
	 * @param listing The listing that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	@Deprecated
	public boolean removePokemonListing(PokemonListing listing) throws IllegalArgumentException {
		if (!hasPokemonListing(listing.getId(), pokemonListings)) {
			throw new IllegalArgumentException("No listing with the UUID " + listing.getId() + " exists.");
		}

		pokemonListings.remove(listing);
		Gts.timers.deleteTimer(listing);
		return writeToFile();
	}

	/**
	 * Method to add a new item listing to the hashmap.
	 * @param listing the listing to add.
	 * @return true if the listing was successfully added.
	 * @throws IllegalArgumentException If the listing already exists.
	 */
	@Deprecated
	public boolean addItemListing(ItemListing listing) throws IllegalArgumentException {
		if (hasItemListing(listing.getId(), getItemListings())) {
			throw new IllegalArgumentException("This listing already exists!");
		}
		itemListings.add(listing);
		Gts.timers.addTimer(listing);
		return writeToFile();
	}

	/**
	 * Method to remove an item listing (expired or bought)
	 * @param listing The listing that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	@Deprecated
	public boolean removeItemListing(ItemListing listing) throws IllegalArgumentException {
		if (!hasItemListing(listing.getId(), getItemListings())) {
			throw new IllegalArgumentException("No listing with the UUID " + listing.getId() + " exists.");
		}

		itemListings.remove(listing);
		Gts.timers.deleteTimer(listing);
		return writeToFile();
	}

	/**
	 * Method to check if a pokemon listing already exists.
	 * @param listing the listing to check for
	 * @param pokemonListings the list to check in
	 * @return true if the listing already exists in the list.
	 */
	@Deprecated
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
	@Deprecated
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
	@Deprecated
	public boolean hasExpiredPokemonListings(UUID playerUUID) {
		return expiredPokemonListings.containsKey(playerUUID);
	}

	/**
	 * Checks that a player has some expired item listings.
	 * @param playerUUID The player to check for expired listings
	 * @return true if the player has expired listings.
	 */
	@Deprecated
	public boolean hasExpiredItemListings(UUID playerUUID) {
		return expiredItemListings.containsKey(playerUUID);
	}

	/**
	 * Method to add an expired pokemon to the list.
	 * @param listing The pokemon listing to add.
	 * @return true if successfully written to file.
	 */
	@Deprecated
	public boolean addExpiredPokemonListing(PokemonListing listing) {
		if (expiredPokemonListings.containsKey(listing.getSellerUuid())) {
			ArrayList<PokemonListing> currentListings = expiredPokemonListings.get(listing.getSellerUuid());
			if (!currentListings.contains(listing)) {
				currentListings.add(listing);
				expiredPokemonListings.put(listing.getSellerUuid(), currentListings);
			}
		} else {
			expiredPokemonListings.put(listing.getSellerUuid(), new ArrayList<>(List.of(listing)));
		}
		return writeToFile();
	}

	/**
	 * Method to add an expired item to the list.
	 * @param listing The item listing to add.
	 * @return true if successfully written to file.
	 */
	@Deprecated
	public boolean addExpiredItemListing(ItemListing listing) {
		if (expiredItemListings.containsKey(listing.getSellerUuid())) {
			ArrayList<ItemListing> currentListings = expiredItemListings.get(listing.getSellerUuid());
			if (!currentListings.contains(listing)) {
				currentListings.add(listing);
				expiredItemListings.put(listing.getSellerUuid(), currentListings);
			}
		} else {
			expiredItemListings.put(listing.getSellerUuid(), new ArrayList<>(List.of(listing)));
		}
		return writeToFile();
	}

	/**
	 * Method to remove an expired listing that has been collected by the player.
	 * @param listing The listing to remove.
	 * @return true if successfully written to file.
	 */
	@Deprecated
	public boolean removeExpiredPokemonListing(PokemonListing listing) {

		if (expiredPokemonListings.get(listing.getSellerUuid()) == null) {
			return false;
		}

		ArrayList<PokemonListing> listings = expiredPokemonListings.get(listing.getSellerUuid());
		if (listings.contains(listing)) {
			listings.remove(listing);
			expiredPokemonListings.put(listing.getSellerUuid(), listings);
		} else {
			return false;
		}
		return writeToFile();
	}

	/**
	 * Method to remove an expired listing that has been collected by the player.
	 * @param listing The listing to remove.
	 * @return true if successfully written to file.
	 */
	@Deprecated
	public boolean removeExpiredItemListing(ItemListing listing) {
		if (expiredItemListings.get(listing.getSellerUuid()) == null) {
			return false;
		}

		ArrayList<ItemListing> listings = expiredItemListings.get(listing.getSellerUuid());
		if (listings.contains(listing)) {
			listings.remove(listing);
			expiredItemListings.put(listing.getSellerUuid(), listings);
		} else {
			return false;
		}
		return writeToFile();
	}

	@Deprecated
	public List<PokemonListing> getExpiredPokemonListings(UUID playerId) {
		return expiredPokemonListings.get(playerId);
	}

	@Deprecated
	public HashMap<UUID, ArrayList<PokemonListing>> getAllExpiredPokemonListings() {
		return expiredPokemonListings;
	}

	@Deprecated
	public HashMap<UUID, ArrayList<ItemListing>> getAllExpiredItemListings() {
		return expiredItemListings;
	}

	@Deprecated
	public List<ItemListing> getExpiredItemListings(UUID playerId) {
		return expiredItemListings.get(playerId);
	}

	/**
	 * Method to write this object to the file.
	 * @return true if the file was successfully written.
	 */
	@Deprecated
	private boolean writeToFile() {

		Gson gson = Utils.newGson();
		String data = gson.toJson(this);

		CompletableFuture<Boolean> future = Utils.writeFileAsync("/config/gts/", "listings.json", data);

		return future.join();
	}

	/**
	 * Method to load the listings from file.
	 */
	@Deprecated
	public void init() {
		try {
			CompletableFuture<Boolean> future = Utils.readFileAsync("/config/gts/", "listings.json", el -> {
				Gson gson = Utils.newGson();
				ListingsProvider data = gson.fromJson(el, ListingsProvider.class);

				for (PokemonListing listing : data.getPokemonListings()) {
					if (listing.getEndTime() > new Date().getTime()) {
						pokemonListings.add(listing);
						Gts.timers.addTimer(listing);
					} else {
						addExpiredPokemonListing(listing);
					}
				}

				for (ItemListing listing : data.getItemListings()) {
					if (listing.getEndTime() > new Date().getTime()) {
						itemListings.add(listing);
						Gts.timers.addTimer(listing);
					} else {
						addExpiredItemListing(listing);
					}
				}

				expiredPokemonListings.putAll(data.getAllExpiredPokemonListings());
				expiredItemListings.putAll(data.getAllExpiredItemListings());
			});


			if (!future.join()) {
				throw new Exception();
			}

		} catch (Exception e) {
			Gts.LOGGER.error("Unable to load listings into memory for " + Gts.MOD_ID + ". Does the file exist?");
		}
	}
}
