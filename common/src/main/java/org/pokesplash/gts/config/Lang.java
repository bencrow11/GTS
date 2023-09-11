package org.pokesplash.gts.config;

import com.google.gson.Gson;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	// placeholders
	// {pokemon}
	// {item}
	// {seller}
	// {buyer}
	// {max_listings}
	// {min_price}
	// {max_price}
	private String title; // Title shown on the top of the UI
	private String purchase_pokemon_message_buyer; // Message that is sent to the buyer of a pokemon
	private String purchase_item_message_buyer; // Message that is sent to the buyer of an item
	private String cancel_pokemon_listing; // Message sent to the player that cancels a pokemon listing.
	private String cancel_item_listing; // Message sent to the player that cancels an item listing
	private String return_pokemon_listing_success; // Message sent to the player that gets a pokemon returned
	private String return_pokemon_listing_fail; // Message sent to the player that gets a failed receive pokemon
	private String return_item_listing_success; // Message sent to the player that gets an item returned
	private String return_item_listing_fail; // Message sent to the player that gets an item returned
	private String maximum_listings; // Message for when a player exceeds their maximum amount of listings.
	private String minimum_listing_amount; // Message to inform the player of the minimum listing amount for that
	private String maximum_listing_price; // Message to inform player of the maximum listing amount.
	private String listing_success_pokemon; // Message to confirm a pokemon listing.
	private String listing_fail_pokemon; // Message to confirm a pokemon listing.
	private String no_pokemon_in_slot; // No pokemon in the given slot.
	private String no_item_in_hand; // No item in the players hand.
	private String item_is_banned; // The item they're trying to list is banned.
	private String not_enough_items; // Not enough items in the players inventory
	private String listing_success_item; // Message to confirm a pokemon listing.
	private String listing_fail_item; // Message to confirm a pokemon listing.
	private String no_item_id_found; // Couldn't find the item ID of the given item.
	private String item_amount_is_zero; // If the item amount given is 0.

	/**
	 * Constructor to generate a file if one doesn't exist.
	 */
	public Lang() {
		title = "Gts";
		purchase_pokemon_message_buyer = "§2You have bought {pokemon} from {seller}!";
		cancel_pokemon_listing = "§6You have cancelled the {pokemon} listing!";
		purchase_item_message_buyer = "§2You have bought {item} from {seller}!";
		cancel_item_listing = "§6You have cancelled the {item} listing!";
		return_pokemon_listing_success = "§2You have received the {pokemon} listing!";
		return_pokemon_listing_fail = "§cCould not receive the {pokemon} listing.";
		return_item_listing_success = "§2You have received the {item} listing!";
		return_item_listing_fail = "§cCould not receive the {item} listing.";
		maximum_listings = "§cYou can only have a total of {max_listings} listings.";
		minimum_listing_amount = "§cYour listing must meet the minimum price of {min_price}";
		maximum_listing_price = "§cYour listing must be below the maximum price of {max_price}";
		listing_success_pokemon = "§2Successfully added your {pokemon} to GTS!";
		listing_fail_pokemon = "§cFailed to add your {pokemon} to GTS!";
		no_pokemon_in_slot = "§cCould not find any Pokemon in the given slot!";
		no_item_in_hand = "§cCould not find an item in your hand!";
		item_is_banned = "§c{item} is banned from GTS!";
		not_enough_items = "§cYou don't have enough {item} in your inventory to list this item!";
		listing_success_item = "§2Successfully added your {item} to GTS!";
		listing_fail_item = "§cFailed to add your {item} to GTS!";
		no_item_id_found = "§cCould not find an item!";
		item_amount_is_zero = "§cListing amount can not be zero!";
	}

	/**
	 * Bunch of getters for the fields.
	 */
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
	public String getReturn_pokemon_listing_success() {
		return return_pokemon_listing_success;
	}
	public String getReturn_item_listing_success() {
		return return_item_listing_success;
	}
	public String getReturn_pokemon_listing_fail() {
		return return_pokemon_listing_fail;
	}
	public String getReturn_item_listing_fail() {
		return return_item_listing_fail;
	}
	public String getMaximum_listings() {
		return maximum_listings;
	}
	public String getMinimum_listing_amount() {
		return minimum_listing_amount;
	}
	public String getMaximum_listing_price() {
		return maximum_listing_price;
	}
	public String getListing_success_pokemon() {
		return listing_success_pokemon;
	}
	public String getListing_fail_pokemon() {
		return listing_fail_pokemon;
	}
	public String getNo_pokemon_in_slot() {
		return no_pokemon_in_slot;
	}
	public String getNo_item_in_hand() {
		return no_item_in_hand;
	}
	public String getItem_is_banned() {
		return item_is_banned;
	}
	public String getNot_enough_items() {
		return not_enough_items;
	}
	public String getListing_success_item() {
		return listing_success_item;
	}
	public String getListing_fail_item() {
		return listing_fail_item;
	}
	public String getNo_item_id_found() {
		return no_item_id_found;
	}
	public String getItem_amount_is_zero() {
		return item_amount_is_zero;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync("/config/gts/", "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					purchase_pokemon_message_buyer = lang.getPurchase_pokemon_message_buyer();
					purchase_item_message_buyer = lang.getPurchase_item_message_buyer();
					cancel_pokemon_listing = lang.getCancel_pokemon_listing();
					cancel_item_listing = lang.getCancel_item_listing();
					return_pokemon_listing_success = lang.getReturn_pokemon_listing_success();
					return_item_listing_success = lang.getReturn_item_listing_success();
					return_pokemon_listing_fail = lang.getReturn_pokemon_listing_fail();
					return_item_listing_fail = lang.getReturn_item_listing_fail();
					maximum_listings = lang.getMaximum_listings();
					minimum_listing_amount = lang.getMinimum_listing_amount();
					maximum_listing_price = lang.getMaximum_listing_price();
					listing_success_pokemon = lang.getListing_success_pokemon();
					listing_fail_pokemon = lang.getListing_fail_pokemon();
					no_pokemon_in_slot = lang.getNo_pokemon_in_slot();
					no_item_in_hand = lang.getNo_item_in_hand();
					item_is_banned = lang.getItem_is_banned();
					not_enough_items = lang.getNot_enough_items();
					listing_success_item = lang.getListing_success_item();
					listing_fail_item = lang.getListing_fail_item();
					no_item_id_found = lang.getNo_item_id_found();
					item_amount_is_zero = lang.getItem_amount_is_zero();
		});

		if (!futureRead.join()) {
			Gts.LOGGER.info("No lang.json file found for GTS. Attempting to generate one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync("/config/gts/", "lang.json", data);

			if (!futureWrite.join()) {
				Gts.LOGGER.fatal("Could not write lang.json for GTS.");
			}
			return;
		}
		Gts.LOGGER.info("GTS lang file read successfully.");
	}
}
