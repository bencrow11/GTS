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
	private String version; // The version of the file.
	private boolean broadcastListings; // Broadcasts new listings to chat.
	private boolean enablePermissionNodes; // Should permission nodes or levels be used.
	private int maxListingsPerPlayer; // The maximum listings each player is allowed.
	private int listingDuration; // The length of each listing.
	private double minPrice1IV; // The minimum price of a pokemon with one full stat (31 IVs)
	private double minPrice2IV; // The minimum price of a pokemon with two full stat (31 IVs)
	private double minPrice3IV; // The minimum price of a pokemon with three full stat (31 IVs)
	private double minPrice4IV; // The minimum price of a pokemon with four full stat (31 IVs)
	private double minPrice5IV; // The minimum price of a pokemon with five full stat (31 IVs)
	private double minPrice6IV; // The minimum price of a pokemon with six full stat (31 IVs)
	private double minPriceHA; // The minimum price of a pokemon with a hidden ability
	private double minPriceLegendary; // The minimum price of a legendary pokemon.
	private double minPriceUltrabeast; // The minimum price of an ultra beast.
	private double maximumPrice; // The maximum price of a listing
	private List<ItemPrices> customItemPrices; // A list of items with minimum prices
	private List<String> bannedItems; // A list of items that can not be sold
	private List<String> bannedPokemon; // A list of pokemon that ca not be sold.

	/**
	 * Constructor to create a default config file.
	 */
	public Config() {
		version = Gts.CONFIG_FILE_VERSION;
		broadcastListings = true;
		enablePermissionNodes = true;
		maxListingsPerPlayer = 8;
		listingDuration = 72;
		minPrice1IV = 10000;
		minPrice2IV = 20000;
		minPrice3IV = 30000;
		minPrice4IV = 40000;
		minPrice5IV = 50000;
		minPrice6IV = 60000;
		minPriceHA = 50000;
		minPriceLegendary = 50000;
		minPriceUltrabeast = 30000;
		maximumPrice = 1000000;
		customItemPrices = new ArrayList<>();
		customItemPrices.add(new ItemPrices());
		bannedItems = new ArrayList<>();
		bannedItems.add("cobblemon:lucky_egg");
		bannedPokemon = new ArrayList<>();
		bannedPokemon.add("magikarp");
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync("/config/gts/", "config.json",
				el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);

					// If the config version isn't correct, update the file.
					if (!cfg.getVersion().equals(Gts.CONFIG_FILE_VERSION)) {
						// TODO Update file (Future)
					}

					broadcastListings = cfg.isBroadcastListings();
					maxListingsPerPlayer = cfg.getMaxListingsPerPlayer();
					listingDuration = cfg.getListingDuration();
					minPrice1IV = cfg.getMinPrice1IV();
					minPrice2IV = cfg.getMinPrice2IV();
					minPrice3IV = cfg.getMinPrice3IV();
					minPrice4IV = cfg.getMinPrice4IV();
					minPrice5IV = cfg.getMinPrice5IV();
					minPrice6IV = cfg.getMinPrice6IV();
					minPriceHA = cfg.getMinPriceHA();
					minPriceLegendary = cfg.getMinPriceLegendary();
					minPriceUltrabeast = cfg.getMinPriceUltrabeast();
					maximumPrice = cfg.getMaximumPrice();
					customItemPrices = cfg.getCustomItemPrices();
					bannedItems = cfg.getBannedItems();
					bannedPokemon = cfg.getBannedPokemon();
					enablePermissionNodes = cfg.isEnablePermissionNodes();
				});

		if (!futureRead.join()) {
			Gts.LOGGER.info("No config.json file found for GTS. Attempting to generate one.");
			CompletableFuture<Boolean> futureWrite = write();

			if (!futureWrite.join()) {
				Gts.LOGGER.fatal("Could not write config for GTS.");
			}
			return;
		}
		Gts.LOGGER.info("GTS config file read successfully.");
	}

	/**
	 * Writes the config to file.
	 * @return Future true if write was successful, otherwise false.
	 */
	public CompletableFuture<Boolean> write() {
		Gson gson = Utils.newGson();
		String data = gson.toJson(this);
		return Utils.writeFileAsync("/config/gts/", "config.json", data);
	}


	/**
	 * Checks the version number of the config file.
	 * @return String that is the config version.
	 */
	public String getVersion() {
		return version;
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
	public int getMaxListingsPerPlayer() {
		return maxListingsPerPlayer;
	}

	/**
	 * Getter for the duration each listing should be up for, in hours.
	 * @return hours per listing as an int.
	 */
	public int getListingDuration() {
		return listingDuration;
	}

	/**
	 * Getter for the min price for a pokemon with 1 full stat (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMinPrice1IV() {
		return minPrice1IV;
	}

	/**
	 * Getter for the min price for a pokemon with 2 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMinPrice2IV() {
		return minPrice2IV;
	}

	/**
	 * Getter for the min price for a pokemon with 3 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMinPrice3IV() {
		return minPrice3IV;
	}

	/**
	 * Getter for the min price for a pokemon with 4 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMinPrice4IV() {
		return minPrice4IV;
	}

	/**
	 * Getter for the min price for a pokemon with 5 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMinPrice5IV() {
		return minPrice5IV;
	}

	/**
	 * Getter for the min price for a pokemon with 6 full stats (31 IVs)
	 * @return minimum price as a double.
	 */
	public double getMinPrice6IV() {
		return minPrice6IV;
	}

	/**
	 * Getter for the min price for a pokemon with a hidden ability.
	 * @return minimum price as a double.
	 */
	public double getMinPriceHA() {
		return minPriceHA;
	}

	/**
	 * Getter for the maximum price of a listing.
	 * @return the maximum price as a double.
	 */
	public double getMaximumPrice() {
		return maximumPrice;
	}

	/**
	 * Getter for the list of items that have minimum prices.
	 * @return list of ItemPrices.
	 */
	public List<ItemPrices> getCustomItemPrices() {
		return customItemPrices;
	}

	/**
	 * Getter for the banned items that can not be listed.
	 * @return List of banned items as strings.
	 */
	public List<String> getBannedItems() {
		return bannedItems;
	}

	/**
	 * If permission nodes are enabled.
	 * @return true if permission nodes are enabled.
	 */
	public boolean isEnablePermissionNodes() {
		return enablePermissionNodes;
	}

	/**
	 * Gets a set of all Pokemon prices.
	 * @return HashSet of all pokemon prices.
	 */
	public HashSet<Double> getAllPokemonPrices() {
		HashSet<Double> prices = new HashSet<>();

		prices.add(minPrice1IV);
		prices.add(minPrice2IV);
		prices.add(minPrice3IV);
		prices.add(minPrice4IV);
		prices.add(minPrice5IV);
		prices.add(minPrice6IV);
		prices.add(minPriceHA);

		return prices;
	}

	/**
	 * Gets a list of all Pokemon that can not be sold.
	 * @return A list of Pokemon names.
	 */
	public List<String> getBannedPokemon() {
		return bannedPokemon;
	}

	/**
	 * Gets the minimum price of a legendary.
	 * @return The minimum price of a legendary.
	 */
	public double getMinPriceLegendary() {
		return minPriceLegendary;
	}

	/**
	 * Gets the minimum price of an ultrabeast.
	 * @return The minimum price of a legendary.
	 */
	public double getMinPriceUltrabeast() {
		return minPriceUltrabeast;
	}
}
