package org.pokesplash.gts.oldVersion;

import com.cobblemon.mod.common.CobblemonItems;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.api.file.Versioned;
import org.pokesplash.gts.util.CodecUtils;
import org.pokesplash.gts.util.Utils;

import java.util.concurrent.CompletableFuture;

public class LangOld extends Versioned {
	// placeholders
	// {listing}
	// {seller}
	// {buyer}
	// {max_listings}
	// {min_price}
	// {max_price}
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
	public LangOld() {
		super(Gts.LANG_FILE_VERSION);
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
	}

	/**
	 * Bunch of getters for the fields.
	 */
	public String getPurchaseMessageBuyer() {
		return purchaseMessageBuyer;
	}
	public String getTitle() {
		return title;
	}
	public String getCancelListing() {
		return cancelListing;
	}
	public String getReturnListingSuccess() {
		return returnListingSuccess;
	}
	public String getReturnListingFail() {
		return returnListingFail;
	}
	public String getMaximumListings() {
		return maximumListings;
	}
	public String getMinimumListingPrice() {
		return minimumListingPrice;
	}
	public String getMaximumListingPrice() {
		return maximumListingPrice;
	}
	public String getListingSuccess() {
		return listingSuccess;
	}
	public String getListingFail() {
		return listingFail;
	}
	public String getNoPokemonInSlot() {
		return noPokemonInSlot;
	}
	public String getNoItemInHand() {
		return noItemInHand;
	}
	public String getBannedItem() {
		return bannedItem;
	}
	public String getInsufficientItems() {
		return insufficientItems;
	}
	public String getItemIdNotFound() {
		return itemIdNotFound;
	}
	public String getZeroItemAmount() {
		return zeroItemAmount;
	}
	public String getReloadMessage() {
		return reloadMessage;
	}
	public String getInsufficientFunds() {
		return insufficientFunds;
	}
	public String getListingBought() {
		return listingBought;
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
		return newListingBroadcast;
	}
	public String getInsufficientInventorySpace() { return insufficientInventorySpace;	}
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
		return bannedPokemon;
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
	public String getOnlyOnePokemonInParty() { return onlyOnePokemonInParty; }

	/**
	 * Method to initialize the config.
	 */
	public void init() {

		CompletableFuture<Boolean> futureRead = read();

		if (!futureRead.join()) {
			Gts.LOGGER.info("No lang.json file found for GTS. Attempting to generate one.");

			CompletableFuture<Boolean> futureWrite = write();

			if (!futureWrite.join()) {
				Gts.LOGGER.fatal("Could not write lang.json for GTS.");
			}
			return;
		}
		Gts.LOGGER.info("GTS lang file read successfully.");
	}

	private CompletableFuture<Boolean> write() {
		Gson gson = Utils.newGson();
		String data = gson.toJson(this);
		return Utils.writeFileAsync("/config/gts/", "lang.json", data);
	}

	private CompletableFuture<Boolean> read() {
		return Utils.readFileAsync("/config/gts/", "lang.json",
				el -> {
					Gson gson = Utils.newGson();

					Versioned versioned = gson.fromJson(el, Versioned.class);

					LangOld lang = gson.fromJson(el, LangOld.class);

					title = lang.getTitle();
					itemTitle = lang.getItemTitle();
					expiredListingsTitle = lang.getExpiredListingsTitle();
					pokemonTitle = lang.getPokemonTitle();
					filteredListingsTitle = lang.getFilteredListingsTitle();
					historyTitle = lang.getHistoryTitle();
					itemListingsTitle = lang.getItemListingsTitle();
					manageTitle = lang.getManageTitle();
					pokemonListingsTitle = lang.getPokemonListingsTitle();
					purchaseMessageBuyer = lang.getPurchaseMessageBuyer();
					cancelListing = lang.getCancelListing();
					relistExpiredButtonLabel = lang.getRelistExpiredButtonLabel();
					returnListingSuccess = lang.getReturnListingSuccess();
					returnListingFail = lang.getReturnListingFail();
					maximumListings = lang.getMaximumListings();
					minimumListingPrice = lang.getMinimumListingPrice();
					maximumListingPrice = lang.getMaximumListingPrice();
					listingSuccess = lang.getListingSuccess();
					listingFail = lang.getListingFail();
					noPokemonInSlot = lang.getNoPokemonInSlot();
					noItemInHand = lang.getNoItemInHand();
					bannedItem = lang.getBannedItem();
					bannedPokemon = lang.getBannedPokemon();
					insufficientItems = lang.getInsufficientItems();
					itemIdNotFound = lang.getItemIdNotFound();
					zeroItemAmount = lang.getZeroItemAmount();
					reloadMessage = lang.getReloadMessage();
					insufficientFunds = lang.getInsufficientFunds();
					listingBought = lang.getListingBought();
					newListingBroadcast = lang.getNewListingBroadcast();
					seller = lang.getSeller();
					price = lang.getPrice();
					remainingTime = lang.getRemainingTime();
					confirmPurchaseButtonLabel = lang.getConfirmPurchaseButtonLabel();
					cancelPurchaseButtonLabel = lang.getCancelPurchaseButtonLabel();
					removeListingButtonLabel = lang.getRemoveListingButtonLabel();
					itemListingsButtonLabel = lang.getItemListingsButtonLabel();
					pokemonListingsButtonLabel = lang.getPokemonListingsButtonLabel();
					manageListingsButtonLabel = lang.getManageListingsButtonLabel();
					nextPageButtonLabel = lang.getNextPageButtonLabel();
					previousPageButtonLabel = lang.getPreviousPageButtonLabel();
					sortByPriceButtonLabel = lang.getSortByPriceButtonLabel();
					sortByNewestButtonLabel = lang.getSortByNewestButtonLabel();
					sortByPokemonButtonLabel = lang.getSortByPokemonButtonLabel();
					sortByNameButtonLabel = lang.getSortByNameButtonLabel();
					receiveListingButtonLabel = lang.getReceiveListingButtonLabel();
					sold_date = lang.getSold_date();
					buyer = lang.getBuyer();
					insufficientInventorySpace = lang.getInsufficientInventorySpace();
					onlyOnePokemonInParty = lang.getOnlyOnePokemonInParty();
					itemListingsButtonItem = CodecUtils.encodeItem(lang.getItemListingsButtonItem());
					pokemonListingsButtonItem = CodecUtils.encodeItem(lang.getPokemonListingsButtonItem());
					manageListingsButtonItem = CodecUtils.encodeItem(lang.getManageListingsButtonItem());
					nextPageButtonItems = CodecUtils.encodeItem(lang.getNextPageButtonItems());
					previousPageButtonItems = CodecUtils.encodeItem(lang.getPreviousPageButtonItems());
					fillerItem = CodecUtils.encodeItem(lang.getFillerItem());
					purchaseButtonItem = CodecUtils.encodeItem(lang.getPurchaseButtonItem());
					cancelButtonItem = CodecUtils.encodeItem(lang.getCancelButtonItem());
					sortByPriceButtonItem = CodecUtils.encodeItem(lang.getSortByPriceButtonItem());
					sortByNewestButtonItem = CodecUtils.encodeItem(lang.getSortByNewestButtonItem());
					sortByNameButtonItem = CodecUtils.encodeItem(lang.getSortByNameButtonItem());
					expiredListingsButtonItem = CodecUtils.encodeItem(lang.getExpiredListingsButtonItem());
					removeListingButtonItem = CodecUtils.encodeItem(lang.getRemoveListingButtonItem());
					relistExpiredButtonItem = CodecUtils.encodeItem(lang.getRelistExpiredButtonItem());
				});
	}
}
