package org.pokesplash.gts.config;

import com.cobblemon.mod.common.CobblemonItems;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.config.options.ItemPrices;
import org.pokesplash.gts.config.options.PokemonAspects;
import org.pokesplash.gts.config.options.PokemonPrices;
import org.pokesplash.gts.config.options.Webhook;
import org.pokesplash.gts.util.CodecUtils;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Config file.
 */
public class Config {
    private int version;
	private boolean enablePokemonSales; // Allows players to sell Pokemon on GTS.
	private boolean enableItemSales; // Allows players to sell Items on GTS.
	private boolean enableAsyncSearches; // Should the /gts command query be run asynchronously.
	private boolean broadcastListings; // Broadcasts new listings to chat.
	private boolean enablePermissionNodes; // Should permission nodes or levels be used.
	private int maxListingsPerPlayer; // The maximum listings each player is allowed.
	private int listingDuration; // The length of each listing.
	private Webhook discord; // Config settings for using discord webhooks.
	private boolean showBreedable; // Should GTS look for "breedable" tag.
	private double taxRate; // How much the seller should be taxed after a sale.
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
	private List<JsonElement> bannedItems; // A list of items that can not be sold
	private List<PokemonPrices> customPokemonPrices; // A list of Pokemon with minimum prices.
	private List<PokemonAspects> bannedPokemon; // A list of pokemon that can not be sold.
    private List<String> removedModDescriptions;

	/**
	 * Constructor to create a default config file.
	 */
	public Config() {
		version = Gts.CONFIG_FILE_VERSION;
		enablePokemonSales = true;
		enableItemSales = true;
		enableAsyncSearches = false;
		broadcastListings = true;
		enablePermissionNodes = true;
		maxListingsPerPlayer = 8;
		listingDuration = 72;
		taxRate = 0.1;
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
		bannedItems.add(CodecUtils.encodeItem(new ItemStack(CobblemonItems.LUCKY_EGG)));
		customPokemonPrices = new ArrayList<>();
		customPokemonPrices.add(new PokemonPrices());
		bannedPokemon = new ArrayList<>();
		bannedPokemon.add(new PokemonAspects());
		discord = new Webhook();
		showBreedable = false;
        removedModDescriptions = new ArrayList<>();
        removedModDescriptions.add("simpletms");
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

    public int getVersion() {
        return version;
    }

	public boolean isBroadcastListings() {
		return broadcastListings;
	}

	public int getMaxListingsPerPlayer() {
		return maxListingsPerPlayer;
	}

	public int getListingDuration() {
		return listingDuration;
	}

	public double getMinPrice1IV() {
		return minPrice1IV;
	}

	public double getMinPrice2IV() {
		return minPrice2IV;
	}

	public double getMinPrice3IV() {
		return minPrice3IV;
	}

	public double getMinPrice4IV() {
		return minPrice4IV;
	}

	public double getMinPrice5IV() {
		return minPrice5IV;
	}

	public double getMinPrice6IV() {
		return minPrice6IV;
	}

	public double getMinPriceHA() {
		return minPriceHA;
	}

	public double getMaximumPrice() {
		return maximumPrice;
	}

	public List<ItemPrices> getCustomItemPrices() {
		return customItemPrices;
	}

	public List<JsonElement> getBannedItems() {
		return bannedItems;
	}

	public boolean isEnablePermissionNodes() {
		return enablePermissionNodes;
	}

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

	public List<PokemonAspects> getBannedPokemon() {
		return bannedPokemon;
	}

	public double getMinPriceLegendary() {
		return minPriceLegendary;
	}

	public double getMinPriceUltrabeast() {
		return minPriceUltrabeast;
	}

	public Webhook getDiscord() {
		return discord;
	}

	public boolean isShowBreedable() {
		return showBreedable;
	}

	public List<PokemonPrices> getCustomPokemonPrices() {
		return customPokemonPrices;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public boolean isEnablePokemonSales() {
		return enablePokemonSales;
	}

	public boolean isEnableItemSales() {
		return enableItemSales;
	}

	public boolean isEnableAsyncSearches() {
		return enableAsyncSearches;
	}

    public List<String> getRemovedModDescriptions() {
        return removedModDescriptions;
    }
}
