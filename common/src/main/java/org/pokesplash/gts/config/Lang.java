package org.pokesplash.gts.config;

import com.google.gson.Gson;
import org.pokesplash.gts.Gts;
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

	private String version; // The version of the file.
	private String title; // Title shown on the top of the UI

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

	private String itemListingsButtonItem; // The display item for the "item listings" menu.
	private String pokemonListingsButtonItem; // The display item for the "pokemon listings" menu.
	private String manageListingsButtonItem; // The display item for the "manage listings" menu.
	private String expiredListingsButtonItem; // The material for the expired listings button.
	private String nextPageButtonItems; // The display item for the "next page" button.
	private String previousPageButtonItems; // The display item for the "previous page" button.
	private String fillerItem; // The filler item for the UI.
	private String purchaseButtonItem; // The material for the purchase button.
	private String cancelButtonItem; // The material for the cancel button.
	private String removeListingButtonItem; // The material for the remove listing moderation button.
	private String sortByPriceButtonItem; // The material for the sort by price button.
	private String sortByNewestButtonItem; // The material for the sort by newest button.
	private String sortByNameButtonItem; // The material for the sort by name button.
	private String relistExpiredButtonItem; // The material for the relist button.

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
		title = "Gts";
		purchaseMessageBuyer = "§2You have bought {listing} from {seller}!";
		relistExpiredButtonLabel = "§9Relist Expired";
		relistExpiredButtonItem = "cobblemon:rare_candy";
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
		itemListingsButtonItem = "cobblemon:assault_vest";
		pokemonListingsButtonItem = "cobblemon:poke_ball";
		manageListingsButtonItem = "cobblemon:sachet";
		nextPageButtonItems = "minecraft:arrow";
		previousPageButtonItems = "cobblemon:poison_barb";
		fillerItem = "minecraft:white_stained_glass_pane";
		purchaseButtonItem = "minecraft:green_stained_glass_pane";
		cancelButtonItem = "minecraft:red_stained_glass_pane";
		sortByPriceButtonItem = "minecraft:gold_nugget";
		sortByNewestButtonItem = "minecraft:clock";
		sortByNameButtonItem = "minecraft:oak_sign";
		expiredListingsButtonItem = "cobblemon:link_cable";
		removeListingButtonItem = "minecraft:orange_stained_glass_pane";
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
	}

	/**
	 * Bunch of getters for the fields.
	 */
	public String getVersion() {
		return version;
	}
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
	public String getItemListingsButtonItem() {
		return itemListingsButtonItem;
	}
	public String getPokemonListingsButtonItem() {
		return pokemonListingsButtonItem;
	}
	public String getManageListingsButtonItem() {
		return manageListingsButtonItem;
	}
	public String getNextPageButtonItems() {
		return nextPageButtonItems;
	}
	public String getPreviousPageButtonItems() {
		return previousPageButtonItems;
	}
	public String getFillerItem() {
		return fillerItem;
	}
	public String getPurchaseButtonItem() {
		return purchaseButtonItem;
	}
	public String getCancelButtonItem() {
		return cancelButtonItem;
	}
	public String getSortByPriceButtonItem() {
		return sortByPriceButtonItem;
	}
	public String getSortByNewestButtonItem() {
		return sortByNewestButtonItem;
	}
	public String getSortByNameButtonItem() {
		return sortByNameButtonItem;
	}
	public String getExpiredListingsButtonItem() {
		return expiredListingsButtonItem;
	}
	public String getRemoveListingButtonItem() {
		return removeListingButtonItem;
	}
	public String getNewListingBroadcast() {
		return newListingBroadcast;
	}
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
	public String getRelistExpiredButtonItem() {
		return relistExpiredButtonItem;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync("/config/gts/", "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);

					// If the lang version isn't correct, update the file.
					if (!lang.getVersion().equals(Gts.LANG_FILE_VERSION)) {
						// TODO Update file (Future)
					}

					title = lang.getTitle();
					purchaseMessageBuyer = lang.getPurchaseMessageBuyer();
					cancelListing = lang.getCancelListing();
					relistExpiredButtonItem = lang.getRelistExpiredButtonItem();
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
					itemListingsButtonItem = lang.getItemListingsButtonItem();
					pokemonListingsButtonItem = lang.getPokemonListingsButtonItem();
					manageListingsButtonItem = lang.getManageListingsButtonItem();
					nextPageButtonItems = lang.getNextPageButtonItems();
					previousPageButtonItems = lang.getPreviousPageButtonItems();
					fillerItem = lang.getFillerItem();
					purchaseButtonItem = lang.getPurchaseButtonItem();
					cancelButtonItem = lang.getCancelButtonItem();
					sortByPriceButtonItem = lang.getSortByPriceButtonItem();
					sortByNewestButtonItem = lang.getSortByNewestButtonItem();
					sortByNameButtonItem = lang.getSortByNameButtonItem();
					expiredListingsButtonItem = lang.getExpiredListingsButtonItem();
					removeListingButtonItem = lang.getRemoveListingButtonItem();
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
