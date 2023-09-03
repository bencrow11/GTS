package org.pokesplash.gts.api;

import net.impactdev.impactor.api.economy.accounts.Account;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;

public abstract class GtsAPI {

	public static boolean cancelListing(PokemonListing listing) {
		boolean success = Gts.listings.removePokemonListing(listing);
		// TODO return pokemon to player.

		return success;
	}

	public static boolean cancelListing(ItemListing listing) {
		boolean success = Gts.listings.removeItemListing(listing);
		// TODO return item to player.

		return success;
	}

	public static boolean addListing(PokemonListing listing) {
		boolean success = Gts.listings.addPokemonListing(listing);
		// TODO Take pokemon from player.

		return success;
	}

	public static boolean addListing(ItemListing listing) {
		boolean success = Gts.listings.addItemListing(listing);
		// TODO Take item from player.

		return success;
	}

	public static boolean sale(ServerPlayer seller, ServerPlayer buyer, PokemonListing listing) {
		boolean listingsSuccess = Gts.listings.removePokemonListing(listing);
		Gts.history.getPlayerHistory(seller.getUUID()).addPokemonListing(listing);

		Account sellerAccount = Gts.impactor.getAccount(seller.getUUID());
		Account buyerAccount = Gts.impactor.getAccount(buyer.getUUID());

		boolean impactorSuccess = Gts.impactor.transfer(buyerAccount, sellerAccount, listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addPokemonListing(listing);

			if (impactorSuccess) {
				Gts.impactor.transfer(sellerAccount, buyerAccount, listing.getPrice());
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

	public static boolean sale(ServerPlayer seller, ServerPlayer buyer, ItemListing listing) {
		boolean listingsSuccess = Gts.listings.removeItemListing(listing);
		Gts.history.getPlayerHistory(seller.getUUID()).addItemListing(listing);

		Account sellerAccount = Gts.impactor.getAccount(seller.getUUID());
		Account buyerAccount = Gts.impactor.getAccount(buyer.getUUID());

		boolean impactorSuccess = Gts.impactor.transfer(buyerAccount, sellerAccount, listing.getPrice());

		// If listing failed to be removed, cancel the transaction.
		if (!listingsSuccess) {
			Gts.listings.addItemListing(listing);

			if (impactorSuccess) {
				Gts.impactor.transfer(sellerAccount, buyerAccount, listing.getPrice());
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
