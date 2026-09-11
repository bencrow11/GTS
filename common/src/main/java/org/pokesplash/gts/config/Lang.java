package org.pokesplash.gts.config;

import com.cobblemon.mod.common.CobblemonItems;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.CodecUtils;
import org.pokesplash.gts.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	// placeholders
	// {listing}
	// {seller}
	// {buyer}
	// {max_listings}
	// {min_price}
	// {max_price}

    private int version;

	/**
	 * Titles
	 */
	private String title; // Title shown on the top of the UI
	private String itemListingsTitle; // Item only page.
	private String pokemonListingsTitle; // Pokemon only page.
	private String expiredListingsTitle; // Title shown for all expired listings.
	private String pokemonTitle; // Title show for expired Pokemon.
	private String itemTitle; // Title shown at the top of the expired item listing.
	private String filteredListingsTitle; // Title shown when using /search
	private String historyTitle; // Title for the history page.
	private String manageTitle; // Title for manage listings page.


	/**
	 * Messages
	 */
	private String purchaseMessageBuyer; // Message that is sent to the buyer of a listing
	private String cancelListing; // Message sent to the player that cancels a listing.
	private String returnListingSuccess; // Message sent to the player that gets a listing returned
	private String returnListingFail; // Message sent to the player that gets a failed receive listing
	private String maximumListings; // Message for when a player exceeds their maximum amount of listings.
	private String minimumListingPrice; // Message to inform the player of the minimum listing amount for that
	private String maximumListingPrice; // Message to inform player of the maximum listing amount.
	private String listingSuccess; // Message to confirm a listing.
	private String listingFail; // Message to confirm a listing.
	private String noPokemonInSlot; // No listing in the given slot.
	private String noItemInHand; // No item in the players hand.
	private String bannedItem; // The item they're trying to list is banned.
	private String bannedPokemon; // The Pokemon they're trying to list is banned.
	private String insufficientItems; // Not enough items in the players inventory
	private String itemIdNotFound; // Couldn't find the item ID of the given item.
	private String zeroItemAmount; // If the item amount given is 0.
	private String reloadMessage; // Message sent when reload command is executed.
	private String insufficientFunds; // Not enough monies.
	private String listingBought; // Message sent to seller when their listing is bought.
	private String newListingBroadcast; // Message sent when a new listing is added.
	private String insufficientInventorySpace; // Message sent when a player doesn't have enough inventory space to receive a listing.
	private String onlyOnePokemonInParty; // Message sent when a player tries to list a Pokemon when they only have one in their party.


	// Prepended to every chat message below. Empty by default so nothing changes
	// for existing servers; set it once here instead of in every single line.
	private String prefix;

	private String playerOnlyCommand; // Sent when a console-only source runs a player command.
	private String genericError; // Generic failure, when the cause is only in the log.
	private String loadingListings; // Shown while the listings are still loading.
	private String listingUnavailable; // The listing was bought or removed while the UI was open.
	private String inBattle; // Tried to list while battling.
	private String untradeablePokemon; // The Pokemon is flagged as untradeable.
	private String timedOut; // The player is under a moderation timeout.
	private String invalidStackAmount; // The requested amount does not divide the held stack.
	private String notHoldingItem; // The command needs an item in hand.
	private String itemNeverSold; // No sale history for that item.
	private String pokemonNeverSold; // No sale history for that Pokemon.
	private String noPokemonFoundInSlot; // Empty party slot.
	private String averagePrice; // Result of the getprice command.
	private String saveItemSuccess; // The item was added to the price list.
	private String saveItemFail; // The item could not be saved.
	private String playerNotFound; // No player matched the given name.
	private String invalidTime; // The time argument could not be read.
	private String timeoutRemoved; // A moderation timeout was lifted.
	private String timeoutAdded; // A moderation timeout was applied.
	private String debugMode; // Feedback for the debug command.

	/**
	 * Command usage
	 */

	private String usageExpired; // Usage line for that subcommand.
	private String usageHistory; // Usage line for that subcommand.
	private String usageGetPrice; // Usage line for that subcommand.
	private String usageDebug; // Usage line for that subcommand.
	private String usageOpen; // Usage line for that subcommand.
	private String usageList; // Usage line for that subcommand.
	private String usageManage; // Usage line for that subcommand.
	private String usageSaveItem; // Usage line for that subcommand.
	private String usageItemDesc; // Usage line for that subcommand.
	private String usagePokemon; // Usage line for that subcommand.
	private String usageItems; // Usage line for that subcommand.
	private String usageReload; // Usage line for that subcommand.
	private String usageSearch; // Usage line for that subcommand.
	private String usageTest; // Usage line for that subcommand.
	private String usageTimeout; // Usage line for that subcommand.

	/**
	 *  Button Labels
	 */

	private String confirmPurchaseButtonLabel; // The name of the "Confirm Purchase" button.
	private String cancelPurchaseButtonLabel; // The name of the "Cancel Purchase" button.
	private String removeListingButtonLabel; // The name of the "Remove Listing" button.
	private String itemListingsButtonLabel; // The name of the "See Item Listings" button.
	private String pokemonListingsButtonLabel; // The name of the "See Pokemon Listings" button.
	private String manageListingsButtonLabel; // The name of the "Manage Listings" button.
	private String nextPageButtonLabel; // The name of the "Next Page" button.
	private String previousPageButtonLabel; // The name of the "Previous Page" button.
	private String sortByPriceButtonLabel; // The name of the "Sort By Price" button.
	private String sortByNewestButtonLabel; // The name of the "Sort By Newest" button.
	private String sortByPokemonButtonLabel; // The name of the "Sort By Pokemon" button.
	private String sortByNameButtonLabel; // The name of the "Sort By Name" button.
	private String receiveListingButtonLabel; // The name of the "Receive Listing" button.
	private String relistExpiredButtonLabel; // The label of the button that relists all expired listings.
	private String expiredListingButtonLabel; // The label for the expired listing button.
	private String pokemonBall; // The label for the ball that the Pokemon is held in.

	/**
	 * Button Materials
	 */

	private JsonElement itemListingsButtonItem; // The display item for the "item listings" menu.
	private JsonElement pokemonListingsButtonItem; // The display item for the "pokemon listings" menu.
	private JsonElement manageListingsButtonItem; // The display item for the "manage listings" menu.
	private JsonElement expiredListingsButtonItem; // The material for the expired listings button.
	private JsonElement nextPageButtonItems; // The display item for the "next page" button.
	private JsonElement previousPageButtonItems; // The display item for the "previous page" button.
	private JsonElement fillerItem; // The filler item for the UI.
	private JsonElement purchaseButtonItem; // The material for the purchase button.
	private JsonElement cancelButtonItem; // The material for the cancel button.
	private JsonElement removeListingButtonItem; // The material for the remove listing moderation button.
	private JsonElement sortByPriceButtonItem; // The material for the sort by price button.
	private JsonElement sortByNewestButtonItem; // The material for the sort by newest button.
	private JsonElement sortByNameButtonItem; // The material for the sort by name button.
	private JsonElement relistExpiredButtonItem; // The material for the relist button.

	/**
	 * Placeholders
	 */

	private String seller; // Seller of the listing.
	private String price; // Price of the listing.
	private String remainingTime; // Time Remaining for the listing.
	private String sold_date; // The date an item was sold.
	private String buyer; // The buyer of a product.



	/**
	 * Constructor to generate a file if one doesn't exist.
	 */
	public Lang() {
        version = Gts.LANG_FILE_VERSION;
		title = "§3Gts";
		expiredListingsTitle = "§3Gts - Expired";
		itemTitle = "§3Gts - Item";
		pokemonTitle = "§3Gts - Pokemon";
		filteredListingsTitle = "§3Gts - %search%";
		historyTitle = "§3Gts - History";
		itemListingsTitle = "§3Gts - Items";
		pokemonListingsTitle = "§3Gts - Pokemon";
		manageTitle = "§3Gts - Manage";
		purchaseMessageBuyer = "§2You have bought {listing} from {seller}!";
		relistExpiredButtonLabel = "§9Relist Expired";
		relistExpiredButtonItem = CodecUtils.encodeItem(new ItemStack(CobblemonItems.RARE_CANDY));
		cancelListing = "§6The {listing} listing has been cancelled!";
		returnListingSuccess = "§2You have received the {listing} listing!";
		returnListingFail = "§cCould not receive the {listing} listing.";
		maximumListings = "§cYou can only have a total of {max_listings} listings.";
		minimumListingPrice = "§cYour listing must meet the minimum price of {min_price}";
		maximumListingPrice = "§cYour listing must be below the maximum price of {max_price}";
		listingSuccess = "§2Successfully added your {listing} to GTS!";
		listingFail = "§cFailed to add your {listing} to GTS!";
		noPokemonInSlot = "§cCould not find any Pokemon in the given slot!";
		noItemInHand = "§cCould not find an item in your hand!";
		bannedItem = "§c{listing} is banned from GTS!";
		bannedPokemon = "§c{listing} is banned from GTS!";
		insufficientItems = "§cYou don't have enough {listing} in your inventory to list this item!";
		itemIdNotFound = "§cCould not find an item!";
		zeroItemAmount = "§cListing amount can not be zero!";
		reloadMessage = "§2Reloaded Configs!";
		insufficientFunds = "§cYou do not have enough money to purchase this listing!";
		listingBought = "§2Your {listing} has been bought by {buyer}";
		itemListingsButtonItem = CodecUtils.encodeItem(new ItemStack(CobblemonItems.ASSAULT_VEST));
		pokemonListingsButtonItem = CodecUtils.encodeItem(new ItemStack(CobblemonItems.POKE_BALL));
		manageListingsButtonItem = CodecUtils.encodeItem(new ItemStack(CobblemonItems.SACHET));
		nextPageButtonItems = CodecUtils.encodeItem(new ItemStack(Items.ARROW));
		previousPageButtonItems = CodecUtils.encodeItem(new ItemStack(CobblemonItems.POISON_BARB));
		fillerItem = CodecUtils.encodeItem(new ItemStack(Items.WHITE_STAINED_GLASS_PANE));
		purchaseButtonItem = CodecUtils.encodeItem(new ItemStack(Items.GREEN_STAINED_GLASS_PANE));
		cancelButtonItem = CodecUtils.encodeItem(new ItemStack(Items.RED_STAINED_GLASS_PANE));
		sortByPriceButtonItem = CodecUtils.encodeItem(new ItemStack(Items.GOLD_NUGGET));
		sortByNewestButtonItem = CodecUtils.encodeItem(new ItemStack(Items.CLOCK));
		sortByNameButtonItem = CodecUtils.encodeItem(new ItemStack(Items.OAK_SIGN));
		expiredListingsButtonItem = CodecUtils.encodeItem(new ItemStack(CobblemonItems.LINK_CABLE));
		removeListingButtonItem = CodecUtils.encodeItem(new ItemStack(Items.ORANGE_STAINED_GLASS_PANE));
		newListingBroadcast = "§e{seller} §2has just added a §e{listing} §2to GTS.";
		seller = "§9Seller: §b";
		price = "§9Price: §b";
		sold_date = "§9Sold Date: §b";
		buyer = "§9Buyer: §b";
		remainingTime = "§9Time Remaining: §b";
		confirmPurchaseButtonLabel = "§2Confirm Purchase";
		cancelPurchaseButtonLabel = "§cCancel Purchase";
		removeListingButtonLabel = "§6Remove Listing";
		itemListingsButtonLabel = "§9See Item Listings";
		pokemonListingsButtonLabel = "§9See Pokemon Listings";
		manageListingsButtonLabel = "§dManage Listings";
		nextPageButtonLabel = "§7Next Page";
		previousPageButtonLabel = "§7Previous Page";
		sortByPriceButtonLabel = "§eSort By Price";
		sortByNewestButtonLabel = "§3Sort By Newest";
		sortByPokemonButtonLabel = "§6Sort By Pokemon";
		sortByNameButtonLabel = "§6Sort By Name";
		receiveListingButtonLabel = "§2Receive Listing";
		insufficientInventorySpace = "§cYou do not have enough inventory space to receive this listing.";
		onlyOnePokemonInParty = "§cYou can not list a Pokemon to GTS if you only have less than 2 Pokemon in your party.";
		prefix = "";
		playerOnlyCommand = "§cThis command must be ran by a player.";
		genericError = "§cSomething went wrong, please tell an admin to check the console.";
		loadingListings = "§2Loading GTS Listings.";
		listingUnavailable = "§cThis listing is no longer available.";
		inBattle = "§cYou can not list to GTS while in a battle.";
		untradeablePokemon = "§cThis Pokemon is not tradeable.";
		timedOut = "§cYou have been timed out for {time}";
		invalidStackAmount = "§cThe stack size can not be divided by amount.";
		notHoldingItem = "§cYou are not holding an item.";
		itemNeverSold = "§cThis item has not been sold before.";
		pokemonNeverSold = "§cThis Pokemon has not been sold before.";
		noPokemonFoundInSlot = "§cNo Pokemon found in slot {slot}.";
		averagePrice = "§aAverage price is ${price}.";
		saveItemSuccess = "§2Item successfully saved.";
		saveItemFail = "§cUnable to save item!";
		playerNotFound = "§cCould not find player with name: {player}";
		invalidTime = "§cCould not parse time {time}";
		timeoutRemoved = "§cRemoved timeout for {player}";
		timeoutAdded = "§b{player} has been timed out for {time}";
		debugMode = "Set Debug mode to {value}";
		usageExpired = "§9Usage:\n§3- gts expired";
		usageHistory = "§9Usage:\n§3- gts history";
		usageGetPrice = "§9Usage:\n§3- gts getprice [slot]";
		usageDebug = "§9Usage:\n§3- gts debug";
		usageOpen = "§9Usage:\n§3- gts <listing id>";
		usageList = "§9Usage:\n§3- gts sell <pokemon/item>";
		usageManage = "§9Usage:\n§3- gts manage";
		usageSaveItem = "§9Usage:\n§3- gts saveitem";
		usageItemDesc = "§9Usage:\n§3- gts itemdesc";
		usagePokemon = "§9Usage:\n§3- gts pokemon";
		usageItems = "§9Usage:\n§3- gts item";
		usageReload = "§9Usage:\n§3- gts reload";
		usageSearch = "§9Usage:\n§3- gts search pokemon/item";
		usageTest = "§9Usage:\n§3- gts test";
		usageTimeout = "§9Usage:\n§3- gts timeout <player> <time> <amount>";
		expiredListingButtonLabel = "§cExpired Listings";
		pokemonBall = "§2Ball: ";
	}



    /**
	 * Bunch of getters for the fields.
	 */
    public int getVersion() {
        return version;
    }
	public String getPurchaseMessageBuyer() {
		return prefix + purchaseMessageBuyer;
	}
	public String getTitle() {
		return title;
	}
	public String getCancelListing() {
		return prefix + cancelListing;
	}
	public String getReturnListingSuccess() {
		return prefix + returnListingSuccess;
	}
	public String getReturnListingFail() {
		return prefix + returnListingFail;
	}
	public String getMaximumListings() {
		return prefix + maximumListings;
	}
	public String getMinimumListingPrice() {
		return prefix + minimumListingPrice;
	}
	public String getMaximumListingPrice() {
		return prefix + maximumListingPrice;
	}
	public String getListingSuccess() {
		return prefix + listingSuccess;
	}
	public String getListingFail() {
		return prefix + listingFail;
	}
	public String getNoPokemonInSlot() {
		return prefix + noPokemonInSlot;
	}
	public String getNoItemInHand() {
		return prefix + noItemInHand;
	}
	public String getBannedItem() {
		return prefix + bannedItem;
	}
	public String getInsufficientItems() {
		return prefix + insufficientItems;
	}
	public String getItemIdNotFound() {
		return prefix + itemIdNotFound;
	}
	public String getZeroItemAmount() {
		return prefix + zeroItemAmount;
	}
	public String getReloadMessage() {
		return prefix + reloadMessage;
	}
	public String getInsufficientFunds() {
		return prefix + insufficientFunds;
	}
	public String getListingBought() {
		return prefix + listingBought;
	}
	public ItemStack getItemListingsButtonItem() {
		return CodecUtils.decodeItem(itemListingsButtonItem);
	}
	public ItemStack getPokemonListingsButtonItem() {
		return CodecUtils.decodeItem(pokemonListingsButtonItem);
	}
	public ItemStack getManageListingsButtonItem() {
		return CodecUtils.decodeItem(manageListingsButtonItem);
	}
	public ItemStack getNextPageButtonItems() {
		return CodecUtils.decodeItem(nextPageButtonItems);
	}
	public ItemStack getPreviousPageButtonItems() {
		return CodecUtils.decodeItem(previousPageButtonItems);
	}
	public ItemStack getFillerItem() {
		return CodecUtils.decodeItem(fillerItem);
	}
	public ItemStack getPurchaseButtonItem() {
		return CodecUtils.decodeItem(purchaseButtonItem);
	}
	public ItemStack getCancelButtonItem() {
		return CodecUtils.decodeItem(cancelButtonItem);
	}
	public ItemStack getSortByPriceButtonItem() {
		return CodecUtils.decodeItem(sortByPriceButtonItem);
	}
	public ItemStack getSortByNewestButtonItem() {
		return CodecUtils.decodeItem(sortByNewestButtonItem);
	}
	public ItemStack getSortByNameButtonItem() {
		return CodecUtils.decodeItem(sortByNameButtonItem);
	}
	public ItemStack getExpiredListingsButtonItem() {
		return CodecUtils.decodeItem(expiredListingsButtonItem);
	}
	public ItemStack getRemoveListingButtonItem() {
		return CodecUtils.decodeItem(removeListingButtonItem);
	}
	public ItemStack getRelistExpiredButtonItem() {
		return CodecUtils.decodeItem(relistExpiredButtonItem);
	}
	public String getNewListingBroadcast() {
		return prefix + newListingBroadcast;
	}
	public String getInsufficientInventorySpace() { return prefix + insufficientInventorySpace; }
	public String getSeller() {
		return seller;
	}
	public String getPrice() {
		return price;
	}
	public String getRemainingTime() {
		return remainingTime;
	}
	public String getConfirmPurchaseButtonLabel() {
		return confirmPurchaseButtonLabel;
	}
	public String getCancelPurchaseButtonLabel() {
		return cancelPurchaseButtonLabel;
	}
	public String getRemoveListingButtonLabel() {
		return removeListingButtonLabel;
	}
	public String getItemListingsButtonLabel() {
		return itemListingsButtonLabel;
	}
	public String getPokemonListingsButtonLabel() {
		return pokemonListingsButtonLabel;
	}
	public String getManageListingsButtonLabel() {
		return manageListingsButtonLabel;
	}
	public String getNextPageButtonLabel() {
		return nextPageButtonLabel;
	}
	public String getPreviousPageButtonLabel() {
		return previousPageButtonLabel;
	}
	public String getSortByPriceButtonLabel() {
		return sortByPriceButtonLabel;
	}
	public String getSortByNewestButtonLabel() {
		return sortByNewestButtonLabel;
	}
	public String getSortByPokemonButtonLabel() {
		return sortByPokemonButtonLabel;
	}
	public String getSortByNameButtonLabel() {
		return sortByNameButtonLabel;
	}
	public String getReceiveListingButtonLabel() {
		return receiveListingButtonLabel;
	}
	public String getSold_date() {
		return sold_date;
	}
	public String getBuyer() {
		return buyer;
	}
	public String getBannedPokemon() {
		return prefix + bannedPokemon;
	}
	public String getRelistExpiredButtonLabel() {
		return relistExpiredButtonLabel;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public String getExpiredListingsTitle() {
		return expiredListingsTitle;
	}
	public String getPokemonTitle() {
		return pokemonTitle;
	}
	public String getFilteredListingsTitle() {
		return filteredListingsTitle;
	}
	public String getHistoryTitle() {
		return historyTitle;
	}
	public String getItemListingsTitle() {
		return itemListingsTitle;
	}
	public String getManageTitle() {
		return manageTitle;
	}
	public String getPokemonListingsTitle() {
		return pokemonListingsTitle;
	}
	public String getOnlyOnePokemonInParty() { return prefix + onlyOnePokemonInParty; }
	public String getExpiredListingButtonLabel() {
		return expiredListingButtonLabel;
	}
	public String getPokemonBall() {
		return pokemonBall;
	}

	public CompletableFuture<Boolean> write() {
		Gson gson = Utils.newGson();
		String data = gson.toJson(this);
		return Utils.writeFileAsync("/config/gts/", "lang.json", data);
	}

	public String getPrefix() {
		return prefix;
	}

	public String getPlayerOnlyCommand() {
		return prefix + playerOnlyCommand;
	}

	public String getGenericError() {
		return prefix + genericError;
	}

	public String getLoadingListings() {
		return prefix + loadingListings;
	}

	public String getListingUnavailable() {
		return prefix + listingUnavailable;
	}

	public String getInBattle() {
		return prefix + inBattle;
	}

	public String getUntradeablePokemon() {
		return prefix + untradeablePokemon;
	}

	public String getTimedOut() {
		return prefix + timedOut;
	}

	public String getInvalidStackAmount() {
		return prefix + invalidStackAmount;
	}

	public String getNotHoldingItem() {
		return prefix + notHoldingItem;
	}

	public String getItemNeverSold() {
		return prefix + itemNeverSold;
	}

	public String getPokemonNeverSold() {
		return prefix + pokemonNeverSold;
	}

	public String getNoPokemonFoundInSlot() {
		return prefix + noPokemonFoundInSlot;
	}

	public String getAveragePrice() {
		return prefix + averagePrice;
	}

	public String getSaveItemSuccess() {
		return prefix + saveItemSuccess;
	}

	public String getSaveItemFail() {
		return prefix + saveItemFail;
	}

	public String getPlayerNotFound() {
		return prefix + playerNotFound;
	}

	public String getInvalidTime() {
		return prefix + invalidTime;
	}

	public String getTimeoutRemoved() {
		return prefix + timeoutRemoved;
	}

	public String getTimeoutAdded() {
		return prefix + timeoutAdded;
	}

	public String getDebugMode() {
		return prefix + debugMode;
	}

	public String getUsageExpired() {
		return usageExpired;
	}

	public String getUsageHistory() {
		return usageHistory;
	}

	public String getUsageGetPrice() {
		return usageGetPrice;
	}

	public String getUsageDebug() {
		return usageDebug;
	}

	public String getUsageOpen() {
		return usageOpen;
	}

	public String getUsageList() {
		return usageList;
	}

	public String getUsageManage() {
		return usageManage;
	}

	public String getUsageSaveItem() {
		return usageSaveItem;
	}

	public String getUsageItemDesc() {
		return usageItemDesc;
	}

	public String getUsagePokemon() {
		return usagePokemon;
	}

	public String getUsageItems() {
		return usageItems;
	}

	public String getUsageReload() {
		return usageReload;
	}

	public String getUsageSearch() {
		return usageSearch;
	}

	public String getUsageTest() {
		return usageTest;
	}

	public String getUsageTimeout() {
		return usageTimeout;
	}
}
