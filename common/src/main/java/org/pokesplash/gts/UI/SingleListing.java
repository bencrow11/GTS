package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.component.ItemLore;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.button.Filler;
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.api.GtsAPI;
import org.pokesplash.gts.util.ColorUtil;
import org.pokesplash.gts.util.Utils;

import java.util.List;

/**
 * UI of the Single Listings page.
 */
public class SingleListing {

	/**
	 * Method that returns the page.
	 * @return SingleListing page.
	 */
	public Page getPage(ServerPlayer viewer, Listing listing) {

		List<Component> lore = ListingInfo.parse(listing);

		if (listing.isPokemon()) {
			lore.addAll(PokemonInfo.parse((PokemonListing) listing));
		}

		Button pokemon = GooeyButton.builder()
				.display(listing.getIcon())
				.with(DataComponents.CUSTOM_NAME, listing.getDisplayName())
				.with(DataComponents.LORE, new ItemLore(lore))
				.build();

		Button purchase = GooeyButton.builder()
				.display(Gts.language.getPurchaseButtonItem())
				.with(DataComponents.CUSTOM_NAME,
						ColorUtil.parse(Gts.language.getConfirmPurchaseButtonLabel()))
				.onClick((action) -> {

					// Checks that the listing still exists.
					if (Gts.listings.getActiveListingById(listing.getId()) == null) {
						action.getPlayer().sendSystemMessage(Component.literal(
								"§cThis listing is no longer available."
						));
						UIManager.closeUI(action.getPlayer());
						return;
					}

					// Checks the player has inventory space for the item.
					if (!listing.isPokemon()) {
						ItemListing itemListing = (ItemListing) listing;
						if (!Utils.hasSpace(action.getPlayer(), itemListing.getListing())) {
							action.getPlayer().sendSystemMessage(
									ColorUtil.parse(Utils.formatPlaceholders(Gts.language.getInsufficientInventorySpace(),
											0, listing.getListingName(), listing.getSellerName(),
											action.getPlayer().getName().getString())));
							UIManager.closeUI(action.getPlayer());
							return;
						}

					}

					// Checks that the player can afford the listing.
					if (!GtsAPI.hasEnoughFunds(action.getPlayer().getUUID(), listing.getPrice())) {
						action.getPlayer().sendSystemMessage(
								ColorUtil.parse(
										Utils.formatPlaceholders(Gts.language.getInsufficientFunds(),
												0, listing.getListingName(), listing.getSellerName(),
												action.getPlayer().getName().getString())));
						return;
					}

					try {
						// Perform the transaction.
						boolean success = GtsAPI.sale(listing.getSellerUuid(), action.getPlayer(), listing);

						// Notify the player.
						action.getPlayer().sendSystemMessage(
								ColorUtil.parse(
										Utils.formatPlaceholders(Gts.language.getPurchaseMessageBuyer(),
												0, listing.getListingName(), listing.getSellerName(),
												action.getPlayer().getName().getString())));

						ServerPlayer seller =
								action.getPlayer().getServer().getPlayerList().getPlayer(listing.getSellerUuid());

						// If the seller is online and not the buyer, notify them.
						if (seller != null && !seller.getUUID().equals(action.getPlayer().getUUID())) {
							seller.sendSystemMessage(
									ColorUtil.parse(
											Utils.formatPlaceholders(Gts.language.getListingBought(),
													0, listing.getListingName(), listing.getSellerName(),
													action.getPlayer().getName().getString())));
						}

					} catch (Exception e) {
						action.getPlayer().sendSystemMessage(Component.literal("§c" + e.getMessage()));
					}


					UIManager.closeUI(action.getPlayer());
				})
				.build();

		Button cancel = GooeyButton.builder()
				.display(Gts.language.getCancelButtonItem())
				.with(DataComponents.CUSTOM_NAME,
						ColorUtil.parse(Gts.language.getCancelPurchaseButtonLabel()))
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new AllListings().getPage();
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button removeListing = GooeyButton.builder()
				.display(Gts.language.getRemoveListingButtonItem())
				.with(DataComponents.CUSTOM_NAME,
						ColorUtil.parse(Gts.language.getRemoveListingButtonLabel()))
				.onClick((action) -> {


					if (action.getPlayer().getUUID().equals(listing.getSellerUuid())) {
						GtsAPI.cancelAndReturnListing(action.getPlayer(), listing);
					} else {
						GtsAPI.cancelListing(listing);
					}
					Component message =
							ColorUtil.parse(
									Utils.formatPlaceholders(Gts.language.getCancelListing(),
									0, listing.getListingName(), listing.getSellerName(),
									action.getPlayer().getName().getString()));

					action.getPlayer().sendSystemMessage(message);

					ServerPlayer seller =
							action.getPlayer().getServer().getPlayerList().getPlayer(listing.getSellerUuid());

					if (seller != null && !seller.getUUID().equals(action.getPlayer().getUUID())) {
						seller.sendSystemMessage(message);
					}

					UIManager.closeUI(action.getPlayer());
				})
				.build();

		ChestTemplate.Builder template = ChestTemplate.builder(3)
				.fill(Filler.getButton())
				.set(13, pokemon)
				.set(15, cancel);

		if (viewer.getUUID().equals(listing.getSellerUuid())) {
			template.set(11, removeListing);
		} else {
			template.set(11, purchase);
		}

		if (Gts.permissions.hasPermission(viewer, "remove")
		&& !viewer.getUUID().equals(listing.getSellerUuid())) {
			template.set(22, removeListing);
		}

		GooeyPage page = GooeyPage.builder()
				.template(template.build())
				.title(listing.getUiTitle())
				.build();

		return page;
	}
}
