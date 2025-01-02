package org.pokesplash.gts.api;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.api.economy.GtsEconomyProvider;
import org.pokesplash.gts.api.event.GtsEvents;
import org.pokesplash.gts.api.event.events.AddEvent;
import org.pokesplash.gts.api.event.events.CancelEvent;
import org.pokesplash.gts.api.event.events.PurchaseEvent;
import org.pokesplash.gts.api.event.events.ReturnEvent;
import org.pokesplash.gts.api.provider.ListingAPI;
import org.pokesplash.gts.discord.Webhook;
import org.pokesplash.gts.util.Utils;

import java.util.UUID;

/**
 * API for interacting with listings.
 */
public abstract class GtsAPI {

	/**
	 * Cancel method that cancels a listing.
	 * @param listing The listing to cancel.
	 * @return true if the listing was successfully cancelled.
	 */
	public static boolean cancelListing(Listing listing) {

		boolean success;

		if (ListingAPI.getHighestPriority() != null) {
			listing.setEndTime(-20);
			ListingAPI.getHighestPriority().update(listing);
			success = true;
		} else {
			success = Gts.listings.removeListing(listing);
			Gts.listings.addExpiredListing(listing);
		}
		GtsEvents.CANCEL.trigger(new CancelEvent(listing));
		return success;
	}

	/**
	 * Add method to add a new pokemon listing.
	 * @param listing The pokemon listing to add.
	 * @return true if the listing was successfully added.
	 */
	public static boolean addListing(PokemonListing listing, ServerPlayer player, int slot) {
		boolean success = Gts.listings.addListing(listing);
		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
		boolean removeSuccess = party.remove(new PartyPosition(slot));

		if (!success || !removeSuccess) {
			Gts.LOGGER.error("Could not list pokemon " + listing.getListing().getSpecies() + " for player: " + player.getUUID());

			if (success) {
				Gts.listings.removeListing(listing);
				listing.delete(Gts.LISTING_FILE_PATH);
			}

			if (removeSuccess) {
				party.add(listing.getListing());
			}
			return false;
		}
		GtsEvents.ADD.trigger(new AddEvent(listing, player));

		if (Gts.config.isBroadcastListings()) {
			Utils.broadcastClickable(Utils.formatPlaceholders(Gts.language.getNewListingBroadcast(), 0,
							listing.getListingName(), listing.getSellerName(), null),
					"/gts " + listing.getId());
		}

		if (Gts.config.getDiscord().isUseWebhooks()) {
			Webhook.newListing(listing);
		}

		return true;
	}

	/**
	 * Add method to add a new item listing.
	 * @param listing The item listing to add.
	 * @return true if the listing was successfully added.
	 */
	public static boolean addListing(ServerPlayer player, ItemListing listing) {
		boolean success = Gts.listings.addListing(listing);
		if (!success) {
			Gts.LOGGER.error("Could not list item " + listing.getListing().getDisplayName().getString() + " for player: " + player.getUUID());
			return false;
		}
		player.getMainHandItem().setCount(player.getMainHandItem().getCount() - listing.getListing().getCount());

		GtsEvents.ADD.trigger(new AddEvent(listing, player));

		if (Gts.config.isBroadcastListings()) {
			Utils.broadcastClickable(Utils.formatPlaceholders(Gts.language.getNewListingBroadcast(), 0,
							listing.getListingName(), listing.getSellerName(), null),
					"/gts " + listing.getId());
		}

		if (Gts.config.getDiscord().isUseWebhooks()) {
			Webhook.newListing(listing);
		}

		return true;
	}

	/**
	 * Method to perform a pokemon sale.
	 * @param seller The person selling the pokemon.
	 * @param buyer The person buying the pokemon.
	 * @param listing The pokemon listing that is being sold.
	 * @return true if the transaction was successful.
	 */
	public static boolean sale(UUID seller, ServerPlayer buyer, PokemonListing listing) {
		boolean listingsSuccess = Gts.listings.removeListing(listing);

		boolean transactionSuccess = GtsEconomyProvider.getHighestEconomy().transfer(buyer.getUUID(), seller, listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addListing(listing);

			if (transactionSuccess) {
				GtsEconomyProvider.getHighestEconomy().transfer(seller, buyer.getUUID(), listing.getPrice());
			}
			return false;
		}

		// If transaction failed, revert the pokemon listing.
		if (!transactionSuccess) {
			Gts.listings.addListing(listing);
			return false;
		}

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(buyer);
		party.add(listing.getListing());

		listing.delete(Gts.LISTING_FILE_PATH);

		Gts.history.addHistoryItem(listing, buyer.getName().getString());

		GtsEvents.PURCHASE.trigger(new PurchaseEvent(buyer, listing));

		if (Gts.config.getDiscord().isUseWebhooks()) {
			Webhook.soldListing(listing);
		}

		return true;
	}

	/**
	 * Method to perform an item sale.
	 * @param seller The person selling the item.
	 * @param buyer The person buying the item.
	 * @param listing The item listing that is being sold.
	 * @return true if the transaction was successful.
	 */
	public static boolean sale(UUID seller, ServerPlayer buyer, ItemListing listing) {

		boolean listingsSuccess = Gts.listings.removeListing(listing);

		boolean transactionSuccess =
				GtsEconomyProvider.getHighestEconomy().transfer(buyer.getUUID(), seller, listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addListing(listing);

			if (transactionSuccess) {
				GtsEconomyProvider.getHighestEconomy().transfer(seller, buyer.getUUID(), listing.getPrice());
			}
			return false;
		}

		// If transaction failed, revert the pokemon listing.
		if (!transactionSuccess) {
			Gts.listings.addListing(listing);
			return false;
		}

		buyer.getInventory().add(listing.getListing());

		listing.delete(Gts.LISTING_FILE_PATH);

		Gts.history.addHistoryItem(listing, buyer.getName().getString());

		GtsEvents.PURCHASE.trigger(new PurchaseEvent(buyer, listing));

		if (Gts.config.getDiscord().isUseWebhooks()) {
			Webhook.soldListing(listing);
		}

		return true;
	}

	/**
	 * Method to return a Pokemon to a player
	 * @param player The player to return the pokemon to
	 * @param listing The listing to return to the player.
	 */
	public static boolean returnListing(ServerPlayer player, PokemonListing listing) {

		if (Gts.listings.removeExpiredListing(listing) && listing.delete(Gts.LISTING_FILE_PATH)) {
			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
			party.add(listing.getListing());
			GtsEvents.RETURN.trigger(new ReturnEvent(player, listing));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method to return an item to a player
	 * @param player The player to return the item to
	 * @param listing The item to be returned.
	 */
	public static boolean returnListing(ServerPlayer player, ItemListing listing) {

		if (player.getInventory().getFreeSlot() == -1) {
			return false;
		}

		if (Gts.listings.removeExpiredListing(listing) && listing.delete(Gts.LISTING_FILE_PATH)) {
			player.getInventory().add(listing.getListing());
			GtsEvents.RETURN.trigger(new ReturnEvent(player, listing));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method to cancel and return a Pokemon to a player
	 * @param player The player to return the pokemon to
	 * @param listing The listing to return to the player.
	 */
	public static boolean cancelAndReturnListing(ServerPlayer player, PokemonListing listing) {
		if (Gts.listings.removeListing(listing)) {
			listing.delete(Gts.LISTING_FILE_PATH);
			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
			party.add(listing.getListing());
			GtsEvents.CANCEL.trigger(new CancelEvent(listing));
			GtsEvents.RETURN.trigger(new ReturnEvent(player, listing));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method to cancel and return an item to a player
	 * @param player The player to return the item to
	 * @param listing The listing to return to the player.
	 */
	public static boolean cancelAndReturnListing(ServerPlayer player, ItemListing listing) {

		if (player.getInventory().getFreeSlot() == -1) {
			return false;
		}

		if (Gts.listings.removeListing(listing)) {
			listing.delete(Gts.LISTING_FILE_PATH);
			player.getInventory().add(listing.getListing());
			GtsEvents.CANCEL.trigger(new CancelEvent(listing));
			GtsEvents.RETURN.trigger(new ReturnEvent(player, listing));
			return true;
		} else {
			return false;
		}
	}
}
