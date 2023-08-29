package org.pokesplash.gts.Listing;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerHistory {
	private UUID player;

	private ArrayList<PokemonListing> pokemonListings;

	private ArrayList<ItemListing> itemListings;

	public PlayerHistory(UUID player, ArrayList<PokemonListing> pokemonListings, ArrayList<ItemListing> itemListings) {
		this.player = player;
		this.pokemonListings = pokemonListings;
		this.itemListings = itemListings;
	}

	public PlayerHistory(UUID player) {
		this.player = player;
		pokemonListings = new ArrayList<>();
		itemListings = new ArrayList<>();
	}

	public UUID getPlayer() {
		return player;
	}

	public ArrayList<PokemonListing> getPokemonListings() {
		return pokemonListings;
	}

	public ArrayList<ItemListing> getItemListings() {
		return itemListings;
	}

	public void addListing(PokemonListing listing) {
		pokemonListings.add(listing);
	}

	public void addListing(ItemListing listing) {
		itemListings.add(listing);
	}
}
