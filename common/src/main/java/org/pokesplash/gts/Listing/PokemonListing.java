package org.pokesplash.gts.Listing;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.pokesplash.gts.util.Utils;

import java.util.UUID;

/**
 * Class that holds a single listing.
 */
public class PokemonListing {
	// The unique ID of the listing.

	private final UUID id;
	// The UUID of the person selling the Pokemon.
	private final UUID sellerUuid;
	// The name of the seller.
	private final String sellerName;
	// The price the Pokemon is selling for.
	private final double price;
	// The time the listing ends.
	private final long endTime;
	// The Pokemon that is being listed.
	private final JsonObject pokemon;

	/**
	 * Constructor to create a new listing.
	 * @param sellerUuid The UUID of the person selling the Pokemon.
	 * @param sellerName The name of the seller.
	 * @param price The price the Pokemon is selling for.
	 * @param endTime The time the listing ends.
	 * @param pokemon The Pokemon for sale.
	 */
	public PokemonListing(UUID sellerUuid, String sellerName, double price, long endTime, Pokemon pokemon) {
		this.id = UUID.randomUUID();
		this.sellerUuid = sellerUuid;
		this.sellerName = sellerName;
		this.price = price;
		this.endTime = endTime;
		this.pokemon = pokemon.saveToJSON(new JsonObject());
	}

	public UUID getId() {
		return id;
	}

	public UUID getSellerUuid() {
		return sellerUuid;
	}

	public String getSellerName() {
		return sellerName;
	}

	public double getPrice() {
		return price;
	}

	public long getEndTime() {
		return endTime;
	}

	public Pokemon getPokemon() {
		return new Pokemon().loadFromJSON(pokemon);
	}
}
