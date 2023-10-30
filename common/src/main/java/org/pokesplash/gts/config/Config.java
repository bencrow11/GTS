package org.pokesplash.gts.config;

import com.google.gson.Gson;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Config file.
 */
public class Config {
	private boolean broadcastListings; // Broadcasts new listings to chat.
	private int max_listings_per_player; // The maximum listings each player is allowed.
	private int listing_duration; // The length of each listing.
	private double min_price_1_IV; // The minimum price of a pokemon with one full stat (31 IVs)
	private double min_price_2_IV; // The minimum price of a pokemon with two full stat (31 IVs)
	private double min_price_3_IV; // The minimum price of a pokemon with three full stat (31 IVs)
	private double min_price_4_IV; // The minimum price of a pokemon with four full stat (31 IVs)
	private double min_price_5_IV; // The minimum price of a pokemon with five full stat (31 IVs)
	private double min_price_6_IV; // The minimum price of a pokemon with six full stat (31 IVs)
	private double min_price_HA; // The minimum price of a pokemon with a hidden ability
	private double maximum_price; // The maximum price of a listing
	private List<ItemPrices> min_item_prices; // A list of items with minimum prices
	private List<String> banned_items; // A list of items that can not be sold

	/**
	 * Constructor to create a default config file.
	 */
	public Config() {
		broadcastListings = true;
		max_listings_per_player = 8;
		listing_duration = 72;
		min_price_1_IV = 10000;
		min_price_2_IV = 20000;
		min_price_3_IV = 30000;
		min_price_4_IV = 40000;
		min_price_5_IV = 50000;
		min_price_6_IV = 60000;
		min_price_HA = 50000;
		maximum_price = 1000000;
		min_item_prices = new ArrayList<>();
		min_item_prices.add(new ItemPrices());
		banned_items = new ArrayList<>();
		banned_items.add("cobblemon:lucky_egg");
	}

	/**
	 * Method to determine if new listings should be broadcasted to all online players.
	 * @return True if listings should be broadcasted.
	 */
	public boolean isBroadcastListings() {
		return broadcastListings;
	}

	/**
	 * Getter for the maximum listings per player.
	 * @return maximum listings as an int.
	 */
	public int getMax_listings_per_player() {
		return max_listings_per_player;
	}

	/**
	 * Getter for the duration each listing should be up for, in hours.
	 * @return hours per listing as an int.
	 */
	public int getListing_duration() {
		return listing_duration;
	}

	/**
	 * Getter for the min price for a pokemon with 1 full stat (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMin_price_1_IV() {
		return min_price_1_IV;
	}

	/**
	 * Getter for the min price for a pokemon with 2 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMin_price_2_IV() {
		return min_price_2_IV;
	}

	/**
	 * Getter for the min price for a pokemon with 3 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMin_price_3_IV() {
		return min_price_3_IV;
	}

	/**
	 * Getter for the min price for a pokemon with 4 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMin_price_4_IV() {
		return min_price_4_IV;
	}

	/**
	 * Getter for the min price for a pokemon with 5 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMin_price_5_IV() {
		return min_price_5_IV;
	}

	/**
	 * Getter for the min price for a pokemon with 6 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMin_price_6_IV() {
		return min_price_6_IV;
	}

	/**
	 * Getter for the min price for a pokemon with a hidden ability.
	 * @return minimum price as a double.
	 */
	public double getMin_price_HA() {
		return min_price_HA;
	}

	/**
	 * Getter for the maximum price of a listing.
	 * @return the maximum price as a double.
	 */
	public double getMaximum_price() {
		return maximum_price;
	}

	/**
	 * Getter for the list of items that have minimum prices.
	 * @return list of ItemPrices.
	 */
	public List<ItemPrices> getMin_item_prices() {
		return min_item_prices;
	}

	/**
	 * Getter for the banned items that can not be listed.
	 * @return List of banned items as strings.
	 */
	public List<String> getBanned_items() {
		return banned_items;
	}

	public HashSet<Double> getAllPokemonPrices() {
		HashSet<Double> prices = new HashSet<>();

		prices.add(min_price_1_IV);
		prices.add(min_price_2_IV);
		prices.add(min_price_3_IV);
		prices.add(min_price_4_IV);
		prices.add(min_price_5_IV);
		prices.add(min_price_6_IV);
		prices.add(min_price_HA);

		return prices;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync("/config/gts/", "config.json",
				el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					broadcastListings = cfg.isBroadcastListings();
					max_listings_per_player = cfg.getMax_listings_per_player();
					listing_duration = cfg.getListing_duration();
					min_price_1_IV = cfg.getMin_price_1_IV();
					min_price_2_IV = cfg.getMin_price_2_IV();
					min_price_3_IV = cfg.getMin_price_3_IV();
					min_price_4_IV = cfg.getMin_price_4_IV();
					min_price_5_IV = cfg.getMin_price_5_IV();
					min_price_6_IV = cfg.getMin_price_6_IV();
					min_price_HA = cfg.getMin_price_HA();
					maximum_price = cfg.getMaximum_price();
					min_item_prices = cfg.getMin_item_prices();
					banned_items = cfg.getBanned_items();
				});

		if (!futureRead.join()) {
			Gts.LOGGER.info("No config.json file found for GTS. Attempting to generate one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync("/config/gts/", "config.json", data);

			if (!futureWrite.join()) {
				Gts.LOGGER.fatal("Could not write config for GTS.");
			}
			return;
		}
		Gts.LOGGER.info("GTS config file read successfully.");
	}
}
