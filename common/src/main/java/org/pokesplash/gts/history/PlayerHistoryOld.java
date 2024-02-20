package org.pokesplash.gts.history;

import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class that holds a players previous sell history.
 */
public class PlayerHistoryOld implements History {
	// The minecraft UUID of the player.
	private UUID player;

	// The previous sold Pokemon.
	private List<PokemonListing> pokemonListings;

	// The previous sold items.
	private List<ItemListing> itemListings;

	/**
	 * Constructor for a new player.
	 * @param playerUUID
	 * @throws IOException
	 */
	public PlayerHistoryOld(UUID playerUUID) {
		player = playerUUID;
		pokemonListings = new ArrayList<>();
		itemListings = new ArrayList<>();
	}

	@Override
	public String version() {
		return null;
	}

	/**
	 * Getter for the player.
	 * @return UUID of the player.
	 */
	public UUID getPlayer() {
		return player;
	}

	/**
	 * Getter for the players pokemon sales.
	 * @return List of pokemon that have been sold.
	 */
	public List<PokemonListing> getPokemonListings() {
		return pokemonListings;
	}

	/**
	 * Getter for the players item sales.
	 * @return List of items that have been sold.
	 */
	public List<ItemListing> getItemListings() {
		return itemListings;
	}

	/**
	 * Method to add a new listing to the players history (Once it has been sold).
	 * @param listing The listing to add.
	 */
	public void addPokemonListing(PokemonListing listing) {
		pokemonListings.add(listing);
	}

	/**
	 * Method to add a new item listing to the players history.
	 * @param listing The listing to add.
	 */
	public void addItemListing(ItemListing listing) {
		itemListings.add(listing);
	}
}
