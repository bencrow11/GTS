package org.pokesplash.gts.config;

public class Lang {
	// placeholders
	// {pokemon}
	// {item}
	// {seller}
	// {buyer}
	private String title;
	private String purchase_message_buyer;
	private String cancel_pokemon_listing;

	public Lang() {
		title = "Gts";
		purchase_message_buyer = "§2You have bought {pokemon} from {seller}!";
		cancel_pokemon_listing = "§6You have cancelled the {pokemon} listing!";
	}

	public String getPurchase_message_buyer() {
		return purchase_message_buyer;
	}

	public String getTitle() {
		return title;
	}

	public String getCancel_pokemon_listing() {
		return cancel_pokemon_listing;
	}
}
