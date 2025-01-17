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
	public static boolean addListing(Listing listing, ServerPlayer player, Integer slot) {
		boolean success = Gts.listings.addListing(listing);
		boolean removeSuccess;

		if (listing.isPokemon()) {
			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
			removeSuccess = party.remove(new PartyPosition(slot));
		} else {
			ItemListing itemListing = (ItemListing) listing;
			player.getMainHandItem().setCount(player.getMainHandItem().getCount() -
					itemListing.getListing().getCount());
			removeSuccess = true;
		}


		if (!success || !removeSuccess) {
			Gts.LOGGER.error("Could not list " + listing.getListingName() + " for player: " + player.getUUID());

			if (success) {
				Gts.listings.removeListing(listing);
				listing.delete(Gts.LISTING_FILE_PATH);
			}

			if (removeSuccess) {
				if (listing.isPokemon()) {
					PokemonListing pokemonListing = (PokemonListing) listing;
					Cobblemon.INSTANCE.getStorage().getParty(player).add(pokemonListing.getListing());
				} else {
					ItemListing itemListing = (ItemListing) listing;
					player.getMainHandItem().setCount(player.getMainHandItem().getCount() +
							itemListing.getListing().getCount());
				}
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
	 * Method to perform a pokemon sale.
	 * @param seller The person selling the pokemon.
	 * @param buyer The person buying the pokemon.
	 * @param listing The pokemon listing that is being sold.
	 * @return true if the transaction was successful.
	 */
	public static boolean sale(UUID seller, ServerPlayer buyer, Listing listing) throws Exception {
		boolean listingsSuccess = Gts.listings.removeListing(listing);

		boolean transactionSuccess = transferFunds(seller, buyer.getUUID(), listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addListing(listing);

			if (transactionSuccess) {
				revertFundTransfer(seller, buyer.getUUID(), listing.getPrice());
			}
			throw new Exception("The listing could not be removed.");
		}

		// If transaction failed, revert the pokemon listing.
		if (!transactionSuccess) {
			Gts.listings.addListing(listing);
			throw new Exception("The transaction encountered a problem.");
		}

		if (listing.isPokemon()) {
			PokemonListing pokemonListing = (PokemonListing) listing;
			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(buyer);
			party.add(pokemonListing.getListing());
		} else {
			ItemListing itemListing = (ItemListing) listing;
			buyer.getInventory().add(itemListing.getListing());
		}

		listing.delete(Gts.LISTING_FILE_PATH);

		Gts.history.addHistoryItem(listing, buyer.getName().getString());

		GtsEvents.PURCHASE.trigger(new PurchaseEvent(buyer, listing));

		if (Gts.config.getDiscord().isUseWebhooks()) {
			Webhook.soldListing(listing);
		}

		return true;
	}

	/**
	 * Transfers funds from one user to another.
	 * @param seller The person receiving the money.
	 * @param buyer The person sending the money.
	 * @param amount The amount to send (Not accounting for tax).
	 * @return true if the transfer was successful, otherwise false.
	 */
	private static boolean transferFunds(UUID seller, UUID buyer, double amount) {
		boolean takeSuccess = GtsEconomyProvider.getHighestEconomy().remove(buyer, amount);
		boolean giveSuccess = GtsEconomyProvider.getHighestEconomy().add(seller,
				(1 - Gts.config.getTaxRate()) * amount);

		if (!takeSuccess && giveSuccess) {
			GtsEconomyProvider.getHighestEconomy().remove(seller,
					(1 - Gts.config.getTaxRate()) * amount);
		}

		if (takeSuccess && !giveSuccess) {
			GtsEconomyProvider.getHighestEconomy().add(buyer, amount);
		}

		return takeSuccess && giveSuccess;
	}

	public static boolean hasEnoughFunds(UUID player, double amount) {
		return GtsEconomyProvider.getHighestEconomy().balance(player) >= amount;
	}

	/**
	 * Reverts a transaction.
	 * @param seller The original seller to remove the paid amount from.
	 * @param buyer The original buyer to send the money back to.
	 * @param amount The transaction amount (not accounting for tax).
	 */
	private static void revertFundTransfer(UUID seller, UUID buyer, double amount) {
		GtsEconomyProvider.getHighestEconomy().add(buyer, amount);
		GtsEconomyProvider.getHighestEconomy().remove(seller,
				(1 - Gts.config.getTaxRate()) * amount);
	}

	/**
	 * Method to return a Listing to a player
	 * @param player The player to return the listing to
	 * @param listing The listing to return to the player.
	 */
	public static boolean returnListing(ServerPlayer player, Listing listing) {

		if (listing.isPokemon()) {

			PokemonListing pokemonListing = (PokemonListing) listing;

			if (Gts.listings.removeExpiredListing(pokemonListing) && pokemonListing.delete(Gts.LISTING_FILE_PATH)) {
				PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
				party.add(pokemonListing.getListing());
				GtsEvents.RETURN.trigger(new ReturnEvent(player, pokemonListing));
				return true;
			} else {
				return false;
			}
		} else {
			ItemListing itemListing = (ItemListing) listing;

			if (player.getInventory().getFreeSlot() == -1) {
				return false;
			}

			if (Gts.listings.removeExpiredListing(itemListing) && itemListing.delete(Gts.LISTING_FILE_PATH)) {
				player.getInventory().add(itemListing.getListing());
				GtsEvents.RETURN.trigger(new ReturnEvent(player, itemListing));
				return true;
			} else {
				return false;
			}
		}

	}

	/**
	 * Method to cancel and return a Listing to a player
	 * @param player The player to return the Listing to
	 * @param listing The listing to return to the player.
	 */
	public static boolean cancelAndReturnListing(ServerPlayer player, Listing listing) {

		if (!listing.isPokemon() && player.getInventory().getFreeSlot() == -1) {
			return false;
		}


		if (Gts.listings.removeListing(listing)) {
			listing.delete(Gts.LISTING_FILE_PATH);

			if (listing.isPokemon()) {
				PokemonListing pokemonListing = (PokemonListing) listing;
				PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
				party.add(pokemonListing.getListing());
			} else {
				ItemListing itemListing = (ItemListing) listing;
				listing.delete(Gts.LISTING_FILE_PATH);
				player.getInventory().add(itemListing.getListing());
			}

			GtsEvents.CANCEL.trigger(new CancelEvent(listing));
			GtsEvents.RETURN.trigger(new ReturnEvent(player, listing));
			return true;
		} else {
			return false;
		}
	}
}
