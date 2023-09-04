package org.pokesplash.gts.api;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.util.ImpactorService;

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
		try {
			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(listing.getSellerUuid());
		} catch (NoPokemonStoreException e) {

		}

		// TODO return pokemon to player.

		return success;
	}

	/**
	 * Cancel method that cancels an item listing.
	 * @param listing The item listing to cancel.
	 * @return true if the listing was successfully cancelled.
	 */
	public static boolean cancelListing(ItemListing listing) {
		boolean success = Gts.listings.removeItemListing(listing);
		// TODO return item to player.

		return success;
	}

	/**
	 * Add method to add a new pokemon listing.
	 * @param listing The pokemon listing to add.
	 * @return true if the listing was successfully added.
	 */
	public static boolean addListing(PokemonListing listing) {
		boolean success = Gts.listings.addPokemonListing(listing);
		// TODO Take pokemon from player.

		return success;
	}

	/**
	 * Add method to add a new item listing.
	 * @param listing The item listing to add.
	 * @return true if the listing was successfully added.
	 */
	public static boolean addListing(ItemListing listing) {
		boolean success = Gts.listings.addItemListing(listing);
		// TODO Take item from player.

		return success;
	}

	/**
	 * Method to perform a pokemon sale.
	 * @param seller The person selling the pokemon.
	 * @param buyer The person buying the pokemon.
	 * @param listing The pokemon listing that is being sold.
	 * @return true if the transaction was successful.
	 */
	public static boolean sale(ServerPlayer seller, ServerPlayer buyer, PokemonListing listing) {
		boolean listingsSuccess = Gts.listings.removePokemonListing(listing);
		Gts.history.getPlayerHistory(seller.getUUID()).addPokemonListing(listing);

		Account sellerAccount = ImpactorService.getAccount(seller.getUUID());
		Account buyerAccount = ImpactorService.getAccount(buyer.getUUID());

		boolean impactorSuccess = ImpactorService.transfer(buyerAccount, sellerAccount, listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addPokemonListing(listing);

			if (impactorSuccess) {
				ImpactorService.transfer(sellerAccount, buyerAccount, listing.getPrice());
			}
		}

		// If transaction failed, revert the pokemon listing.
		if (!impactorSuccess) {
			if (listingsSuccess) {
				Gts.listings.addPokemonListing(listing);
			}
		}

		// TODO check transaction success before giving pokemon.
		// TODO Give Pokemon to buyer.
		return listingsSuccess && impactorSuccess;
	}

	/**
	 * Method to perform an item sale.
	 * @param seller The person selling the item.
	 * @param buyer The person buying the item.
	 * @param listing The item listing that is being sold.
	 * @return true if the transaction was successful.
	 */
	public static boolean sale(ServerPlayer seller, ServerPlayer buyer, ItemListing listing) {
		boolean listingsSuccess = Gts.listings.removeItemListing(listing);
		Gts.history.getPlayerHistory(seller.getUUID()).addItemListing(listing);

		Account sellerAccount = ImpactorService.getAccount(seller.getUUID());
		Account buyerAccount = ImpactorService.getAccount(buyer.getUUID());

		boolean impactorSuccess = ImpactorService.transfer(buyerAccount, sellerAccount, listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addItemListing(listing);

			if (impactorSuccess) {
				ImpactorService.transfer(sellerAccount, buyerAccount, listing.getPrice());
			}
		}

		// If transaction failed, revert the pokemon listing.
		if (!impactorSuccess) {
			if (listingsSuccess) {
				Gts.listings.addItemListing(listing);
			}
		}

		// TODO check transaction success before giving item.
		// TODO Give Pokemon to buyer.
		return listingsSuccess && impactorSuccess;
	}
}