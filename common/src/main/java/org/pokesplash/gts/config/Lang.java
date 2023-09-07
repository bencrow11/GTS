package org.pokesplash.gts.config;

public class Lang {
	// placeholders
	// {pokemon}
	// {item}
	// {seller}
	// {buyer}
	private String title; // Title shown on the top of the UI
	private String purchase_pokemon_message_buyer; // Message that is sent to the buyer of a pokemon
	private String purchase_item_message_buyer; // Message that is sent to the buyer of an item
	private String cancel_pokemon_listing; // Message sent to the player that cancels a pokemon listing.
	private String cancel_item_listing; // Message sent to the player that cancels an item listing
	private String return_pokemon_listing; // Message sent to the player that gets a pokemon returned
	private String return_item_listing; // Message sent to the player that gets an item returned

	public Lang() {
		title = "Gts";
		purchase_pokemon_message_buyer = "§2You have bought {pokemon} from {seller}!";
		cancel_pokemon_listing = "§6You have cancelled the {pokemon} listing!";
		purchase_item_message_buyer = "§2You have bought {item} from {seller}!";
		cancel_item_listing = "§6You have cancelled the {item} listing!";
		return_pokemon_listing = "§6You have received the {pokemon} listing!";
		return_item_listing = "§6You have received the {item} listing!";
	}

	public String getPurchase_pokemon_message_buyer() {
		return purchase_pokemon_message_buyer;
	}

	public String getTitle() {
		return title;
	}

	public String getCancel_pokemon_listing() {
		return cancel_pokemon_listing;
	}

	public String getPurchase_item_message_buyer() {
		return purchase_item_message_buyer;
	}

	public String getCancel_item_listing() {
		return cancel_item_listing;
	}

	public String getReturn_pokemon_listing() {
		return return_pokemon_listing;
	}

	public String getReturn_item_listing() {
		return return_item_listing;
	}
}
