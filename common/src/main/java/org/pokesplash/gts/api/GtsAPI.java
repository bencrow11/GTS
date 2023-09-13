package org.pokesplash.gts.api;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PartyPosition;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.history.PlayerHistory;
import org.pokesplash.gts.util.ImpactorService;

import java.util.UUID;

/**
 * API for interacting with listings.
 */
public abstract class GtsAPI {

	/**
	 * Cancel method that cancels a pokemon listing.
	 * @param listing The Pokemon listing to cancel.
	 * @return true if the listing was successfully cancelled.
	 */
	public static boolean cancelListing(PokemonListing listing) {
		boolean success = Gts.listings.removePokemonListing(listing);
		Gts.listings.addExpiredPokemonListing(listing);
		return success;
	}

	/**
	 * Cancel method that cancels an item listing.
	 * @param listing The item listing to cancel.
	 * @return true if the listing was successfully cancelled.
	 */
	public static boolean cancelListing(ItemListing listing) {
		boolean success = Gts.listings.removeItemListing(listing);
		Gts.listings.addExpiredItemListing(listing);
		return success;
	}

	/**
	 * Add method to add a new pokemon listing.
	 * @param listing The pokemon listing to add.
	 * @return true if the listing was successfully added.
	 */
	public static boolean addListing(PokemonListing listing, ServerPlayer player, int slot) {
		boolean success = Gts.listings.addPokemonListing(listing);
		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
		boolean removeSuccess = party.remove(new PartyPosition(slot));

		if (!success || !removeSuccess) {
			Gts.LOGGER.error("Could not list pokemon " + listing.getPokemon().getSpecies() + " for player: " + player.getUUID());

			if (success) {
				Gts.listings.removePokemonListing(listing);
			}

			if (removeSuccess) {
				party.add(listing.getPokemon());
			}
			return false;
		}

		return true;
	}

	/**
	 * Add method to add a new item listing.
	 * @param listing The item listing to add.
	 * @return true if the listing was successfully added.
	 */
	public static boolean addListing(ServerPlayer player, ItemListing listing) {
		boolean success = Gts.listings.addItemListing(listing);
		if (!success) {
			Gts.LOGGER.error("Could not list item " + listing.getItem().getDisplayName().getString() + " for player: " + player.getUUID());
			return false;
		}
		player.getMainHandItem().setCount(player.getMainHandItem().getCount() - listing.getItem().getCount());
		return true;
	}

	/**
	 * Method to perform a pokemon sale.
	 * @param seller The person selling the pokemon.
	 * @param buyer The person buying the pokemon.
	 * @param listing The pokemon listing that is being sold.
	 * @return true if the transaction was successful.
	 */
	public static boolean sale(UUID seller, UUID buyer, PokemonListing listing) {
		boolean listingsSuccess = Gts.listings.removePokemonListing(listing);
		Account sellerAccount = ImpactorService.getAccount(seller);
		Account buyerAccount = ImpactorService.getAccount(buyer);

		boolean impactorSuccess = ImpactorService.transfer(buyerAccount, sellerAccount, listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addPokemonListing(listing);

			if (impactorSuccess) {
				ImpactorService.transfer(sellerAccount, buyerAccount, listing.getPrice());
			}
			return false;
		}

		// If transaction failed, revert the pokemon listing.
		if (!impactorSuccess) {
			Gts.listings.addPokemonListing(listing);
			return false;
		}

		try {
			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(buyer);
			party.add(listing.getPokemon());
		} catch (NoPokemonStoreException e) {
			Gts.LOGGER.error("Could not give pokemon " + listing.getPokemon().getSpecies() + " to player: " + listing.getSellerName() +
					".\nError: " + e.getMessage());
		}

		if (Gts.history.getPlayerHistory(seller) == null) {
			new PlayerHistory(seller);
		}
		Gts.history.getPlayerHistory(seller).addPokemonListing(listing);

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
		boolean listingsSuccess = Gts.listings.removeItemListing(listing);



		Account sellerAccount = ImpactorService.getAccount(seller);
		Account buyerAccount = ImpactorService.getAccount(buyer.getUUID());

		boolean impactorSuccess = ImpactorService.transfer(buyerAccount, sellerAccount, listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addItemListing(listing);

			if (impactorSuccess) {
				ImpactorService.transfer(sellerAccount, buyerAccount, listing.getPrice());
			}
			return false;
		}

		// If transaction failed, revert the pokemon listing.
		if (!impactorSuccess) {
			Gts.listings.addItemListing(listing);
			return false;
		}

		buyer.getInventory().add(listing.getItem());

		if (Gts.history.getPlayerHistory(seller) == null) {
			new PlayerHistory(seller);
		}
		Gts.history.getPlayerHistory(seller).addItemListing(listing);

		return true;
	}

	/**
	 * Method to return a Pokemon to a player
	 * @param player The player to return the pokemon to
	 * @param listing The listing to return to the player.
	 */
	public static boolean returnListing(ServerPlayer player, PokemonListing listing) {

		if (Gts.listings.removeExpiredPokemonListing(listing)) {
			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
			party.add(listing.getPokemon());
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
		if (Gts.listings.removeExpiredItemListing(listing)) {
			player.getInventory().add(listing.getItem());
			return true;
		} else {
			return false;
		}
	}
}
