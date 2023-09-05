package org.pokesplash.gts.Listing;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.google.gson.Gson;
import dev.architectury.event.Event;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.registries.forge.RegistriesImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Manages all types of listings. Data is saved to memory here.
 */
public class ListingsProvider {
	// All active pokemon listings.
	private List<PokemonListing> pokemonListings;
	// All active item listings.
	private List<ItemListing> itemListings;
	// Where listings are stored once they have expired.
	private HashMap<UUID, List<PokemonListing>> expiredPokemonListings;
	private HashMap<UUID, List<ItemListing>> expiredItemListings;

	/**
	 * Constructor to create a new list for both hashmaps.
	 */
	public ListingsProvider() {
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
		Gts.timers.addTimer(listing);
		return writeToFile();
	}

	/**
	 * Method to remove a pokemon listing (expired or bought)
	 * @param listing The listing that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	public boolean removePokemonListing(PokemonListing listing) throws IllegalArgumentException {
		if (!hasPokemonListing(listing.getId(), pokemonListings)) {
			throw new IllegalArgumentException("No listing with the UUID " + listing.getId() + " exists.");
		}

		pokemonListings.remove(listing);
		Gts.timers.deleteTimer(listing);
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
		Gts.timers.addTimer(listing);
		return writeToFile();
	}

	/**
	 * Method to remove an item listing (expired or bought)
	 * @param listing The listing that should be removed.
	 * @return true if the listing was successfully removed.
	 * @throws IllegalArgumentException if the listing doesn't exist.
	 */
	public boolean removeItemListing(ItemListing listing) throws IllegalArgumentException {
		if (!hasItemListing(listing.getId(), itemListings)) {
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

	/**
	 * Method to add an expired pokemon to the list.
	 * @param listing The pokemon listing to add.
	 * @return true if successfully written to file.
	 */
	public boolean addExpiredPokemonListing(PokemonListing listing) {
		if (expiredPokemonListings.containsKey(listing.getSellerUuid())) {
			List<PokemonListing> currentListings = expiredPokemonListings.get(listing.getSellerUuid());
			currentListings.add(listing);
			expiredPokemonListings.put(listing.getSellerUuid(), currentListings);
		} else {
			expiredPokemonListings.put(listing.getSellerUuid(), List.of(listing));
		}
		return writeToFile();
	}

	/**
	 * Method to add an expired item to the list.
	 * @param listing The item listing to add.
	 * @return true if successfully written to file.
	 */
	public boolean addExpiredItemListing(ItemListing listing) {
		if (expiredItemListings.containsKey(listing.getSellerUuid())) {
			List<ItemListing> currentListings = expiredItemListings.get(listing.getSellerUuid());
			currentListings.add(listing);
			expiredItemListings.put(listing.getSellerUuid(), currentListings);
		} else {
			expiredItemListings.put(listing.getSellerUuid(), List.of(listing));
		}
		return writeToFile();
	}

	/**
	 * Method to remove an expired listing that has been collected by the player.
	 * @param listing The listing to remove.
	 * @return true if successfully written to file.
	 */
	public boolean removeExpiredPokemonListing(PokemonListing listing) {
		List<PokemonListing> listings = expiredPokemonListings.get(listing.getSellerUuid());
		if (!listings.isEmpty()) {
			listings.remove(listing);
			expiredPokemonListings.put(listing.getSellerUuid(), listings);
		} else {
			expiredPokemonListings.remove(listing.getSellerUuid());
		}
		return writeToFile();
	}

	/**
	 * Method to remove an expired listing that has been collected by the player.
	 * @param listing The listing to remove.
	 * @return true if successfully written to file.
	 */
	public boolean removeExpiredItemListing(ItemListing listing) {
		List<ItemListing> listings = expiredItemListings.get(listing.getSellerUuid());
		if (!listings.isEmpty()) {
			listings.remove(listing);
			expiredItemListings.put(listing.getSellerUuid(), listings);
		} else {
			expiredItemListings.remove(listing.getSellerUuid());
		}
		return writeToFile();
	}

	public List<PokemonListing> getExpiredPokemonListings(UUID playerId) {
		return expiredPokemonListings.get(playerId);
	}

	public List<ItemListing> getExpiredItemListings(UUID playerId) {
		return expiredItemListings.get(playerId);
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

	/**
	 * Method to load the listings from file.
	 */
	public void init() {
		try {
			CompletableFuture<Boolean> future = Utils.readFileAsync("/config/gts/", "listings.json", el -> {
				Gson gson = Utils.newGson();
				ListingsProvider data = gson.fromJson(el, ListingsProvider.class);
				pokemonListings = data.getPokemonListings();
				itemListings = data.getItemListings();
			});

			if (!future.join()) {
				throw new Exception();
			}

		} catch (Exception e) {
			Gts.LOGGER.error("Unable to load listings into memory for " + Gts.MOD_ID + ". Does the file exist?");
		}

		for (PokemonListing listing : pokemonListings) {
			Gts.timers.addTimer(listing);
		}

		for (ItemListing listing : itemListings) {
			Gts.timers.addTimer(listing);
		}
	}

	/**
	 * Method used for when a player joins to return their expired listings.
	 * @param player The player to return their expired listings to.
	 */
	public void playerJoinedEvent(ServerPlayer player) {

			List<PokemonListing> expiredPokemon = getExpiredPokemonListings(player.getUUID());

			for (PokemonListing listing : expiredPokemon) {
				try {
					PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player.getUUID());
					party.add(listing.getPokemon());
					removeExpiredPokemonListing(listing);
				} catch (NoPokemonStoreException e) {
					Gts.LOGGER.error("Could not give pokemon " + listing.getPokemon().getSpecies() + " to player: " + listing.getSellerName() +
							".\nError: " + e.getMessage());
				}

			}

			List<ItemListing> expiredItems = getExpiredItemListings(player.getUUID());

			for (ItemListing listing : expiredItems) {
				player.getInventory().add(new ItemStack(listing.getItem(), listing.getAmount()));
			}
	}
}
