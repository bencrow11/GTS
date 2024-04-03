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
	private List<Listing> listings;

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
		player = playerHistoryOld.getPlayer();
		listings = new ArrayList<>();

        listings.addAll(playerHistoryOld.getPokemonListings());
		listings.addAll(playerHistoryOld.getItemListings());

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
	public List<Listing> getListings() {
		return listings;
	}

	/**
	 * Getter for the players pokemon sales.
	 * @return List of pokemon that have been sold.
	 */
	public List<PokemonListing> getPokemonListings() {

		ArrayList<PokemonListing> pokemonListings = new ArrayList<>();

		for (Listing listing : listings) {
			if (listing instanceof PokemonListing) {
				pokemonListings.add((PokemonListing) listing);
			}
		}

		return pokemonListings;
	}

	/**
	 * Getter for the players item sales.
	 * @return List of items that have been sold.
	 */
	public List<ItemListing> getItemListings() {

		ArrayList<ItemListing> itemListings = new ArrayList<>();

		for (Listing listing : listings) {
			if (listing instanceof ItemListing) {
				itemListings.add((ItemListing) listing);
			}
		}

		return itemListings;
	}

	/**
	 * Method to add a new listing to the players history (Once it has been sold).
	 * @param listing The listing to add.
	 */
	public void addListing(Listing listing) {
		listings.add(listing);
		Gts.history.updatePlayerHistory(this);
	}
}
