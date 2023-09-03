package org.pokesplash.gts.history;

import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

/**
 * Class that holds a players previous sell history.
 */
public class PlayerHistory {
	// The minecraft UUID of the player.
	private UUID player;

	// The previous sold Pokemon.
	private HashSet<PokemonListing> pokemonListings;

	// The previous sold items.
	private HashSet<ItemListing> itemListings;

	/**
	 * Constructor for a new player.
	 * @param playerUUID
	 * @throws IOException
	 */
	public PlayerHistory(UUID playerUUID) {
		player = playerUUID;
		pokemonListings = new HashSet<>();
		itemListings = new HashSet<>();
		Gts.history.updatePlayerHistory(this);
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
	public HashSet<PokemonListing> getPokemonListings() {
		return pokemonListings;
	}

	/**
	 * Getter for the players item sales.
	 * @return List of items that have been sold.
	 */
	public HashSet<ItemListing> getItemListings() {
		return itemListings;
	}

	/**
	 * Method to add a new listing to the players history (Once it has been sold).
	 * @param listing The listing to add.
	 */
	public void addPokemonListing(PokemonListing listing) {
		pokemonListings.add(listing);
		Gts.history.updatePlayerHistory(this);
	}

	/**
	 * Method to add a new item listing to the players history.
	 * @param listing The listing to add.
	 */
	public void addItemListing(ItemListing listing) {
		itemListings.add(listing);
		Gts.history.updatePlayerHistory(this);
	}
}
