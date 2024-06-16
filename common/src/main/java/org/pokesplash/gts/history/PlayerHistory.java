package org.pokesplash.gts.history;

import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.api.provider.HistoryAPI;
import org.pokesplash.gts.oldVersion.PlayerHistoryOld;
import org.pokesplash.gts.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class that holds a players previous sell history.
 */
public class PlayerHistory implements History {
	// The minecraft UUID of the player.
	private UUID player;

	// History of sold listings
	private List<HistoryItem> listings;

	/**
	 * Constructor for a new player.
	 * @param playerUUID
	 * @throws IOException
	 */
	public PlayerHistory(UUID playerUUID) {
		player = playerUUID;
		listings = new ArrayList<>();
		if (HistoryAPI.getHighestPriority() == null) {
			Utils.checkForDirectory(HistoryProvider.filePath + playerUUID + "/");
		}
	}

	/**
	 * Used to initialise the object once the files have been read.
	 * @param player The player whose history this is.
	 * @param items The players history.
	 */
	public PlayerHistory(UUID player, ArrayList<HistoryItem> items) {
		this.player = player;
		listings = items;
	}

	/**
	 * Used to update an old player history to a new one.
	 * @param playerHistoryOld The old player history object to convert.
	 */
	public PlayerHistory(PlayerHistoryOld playerHistoryOld) {
		player = playerHistoryOld.getPlayer();
		listings = new ArrayList<>();

		for (PokemonListing listing : playerHistoryOld.getPokemonListings()) {
			listing.update(true);
			PokemonHistoryItem item = new PokemonHistoryItem(listing, "Unknown");
			item.write();
			listings.add(item);
		}

		for (ItemListing listing : playerHistoryOld.getItemListings()) {
			listing.update(false);
			ItemHistoryItem item = new ItemHistoryItem(listing, "Unknown");
			item.write();
			listings.add(item);
		}
	}

	/**
	 * Getter for the player.
	 * @return UUID of the player.
	 */
	public UUID getPlayer() {
		return player;
	}

	/**
	 * Getter for the players sales.
	 * @return List of listings that have been sold.
	 */
	public List<HistoryItem> getListings() {
		return listings;
	}

	/**
	 * Getter for the players pokemon sales.
	 * @return List of pokemon that have been sold.
	 */
	public List<PokemonHistoryItem> getPokemonListings() {

		ArrayList<PokemonHistoryItem> pokemonListings = new ArrayList<>();

		for (HistoryItem listing : listings) {
			if (listing.isPokemon()) {
				pokemonListings.add((PokemonHistoryItem) listing);
			}
		}

		return pokemonListings;
	}

	/**
	 * Getter for the players item sales.
	 * @return List of items that have been sold.
	 */
	public List<ItemHistoryItem> getItemListings() {

		ArrayList<ItemHistoryItem> itemListings = new ArrayList<>();

		for (HistoryItem listing : listings) {
			if (!listing.isPokemon()) {
				itemListings.add((ItemHistoryItem) listing);
			}
		}

		return itemListings;
	}

	/**
	 * Method to add a new listing to the players history (Once it has been sold).
	 * @param listing The listing to add.
	 */
	public void addListing(Listing listing, String buyerName) {
		HistoryItem item = listing.isPokemon() ? new PokemonHistoryItem((PokemonListing) listing, buyerName) :
				new ItemHistoryItem((ItemListing) listing, buyerName);
		item.write();
		listings.add(item);
	}

	/**
	 * Method to add a history item to memory.
	 * @param historyItem The history item to add.
	 */
	public void addHistory(HistoryItem historyItem) {
		listings.add(historyItem);
	}

	/**
	 * Method to write all player listings to file.
	 */
	public void writeAll() {
		for (HistoryItem item : listings) {
			item.write();
		}
	}

}
