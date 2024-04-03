package org.pokesplash.gts.history;

import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.oldVersion.PlayerHistoryOld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class that holds a players previous sell history.
 */
public class PlayerHistory implements History {
	private String version; // The current file version.
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
		version = Gts.HISTORY_FILE_VERSION;
		player = playerUUID;
		listings = new ArrayList<>();
		Gts.history.updatePlayerHistory(this);
	}

	/**
	 * Used to update an old player history to a new one.
	 * @param playerHistoryOld The old player history object to convert.
	 */
	public PlayerHistory(PlayerHistoryOld playerHistoryOld) {
		version = Gts.HISTORY_FILE_VERSION;

		player = playerHistoryOld.getPlayer();
		listings = new ArrayList<>();

		for (PokemonListing listing : playerHistoryOld.getPokemonListings()) {
			listing.update(true);
			listings.add(new PokemonHistoryItem(listing, "Unknown"));
		}

		for (ItemListing listing : playerHistoryOld.getItemListings()) {
			listing.update(false);
			listings.add(new ItemHistoryItem(listing, "Unknown"));
		}

		Gts.history.updatePlayerHistory(this);
	}


	@Override
	public String version() {
		return version;
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
		listings.add(listing.isPokemon() ? new PokemonHistoryItem((PokemonListing) listing, buyerName) :
				new ItemHistoryItem((ItemListing) listing, buyerName));
		Gts.history.updatePlayerHistory(this);
	}
}
