package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.api.GtsAPI;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * UI of the Single Item Listing
 */
public class SingleItemListing {

	/**
	 * Method that returns the page.
	 * @return SinglePokemonListing page.
	 */
	public Page getPage(ServerPlayer viewer, ItemListing listing) {

		Collection<Component> lore = ListingInfo.parse(listing);

		Button pokemon = GooeyButton.builder()
				.display(listing.getListing())
				.title("§3" + Utils.capitaliseFirst(listing.getListing().getDisplayName().getString()))
				.lore(Component.class, lore)
				.build();

		Button purchase = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getPurchaseButtonItem()))
				.title(Gts.language.getConfirmPurchaseButtonLabel())
				.onClick((action) -> {
					String message;
          if (Gts.listings.getActiveListingById(listing.getId()) == null) {
            message = "§cThis item is no longer available.";
					} else if (action.getPlayer().getInventory().getFreeSlot() == -1)
						message = Utils.formatPlaceholders(Gts.language.getInsufficientInventorySpace(),
								0, listing.getListing().getDisplayName().getString(), listing.getSellerName(),
								action.getPlayer().getName().getString());
					else {
						boolean success = GtsAPI.sale(listing.getSellerUuid(), action.getPlayer(), listing);

						if (success) {
							message = Utils.formatPlaceholders(Gts.language.getPurchaseMessageBuyer(),
									0, listing.getListing().getDisplayName().getString(), listing.getSellerName(),
									action.getPlayer().getName().getString());

							ServerPlayer seller =
									action.getPlayer().getServer().getPlayerList().getPlayer(listing.getSellerUuid());

							if (seller != null && !seller.getUUID().equals(action.getPlayer().getUUID())) {
								seller.sendSystemMessage(Component.literal(
										Utils.formatPlaceholders(Gts.language.getListingBought(),
												0, listing.getListing().getDisplayName().getString(), listing.getSellerName(),
												action.getPlayer().getName().getString())));
							}
						} else {
							message = Utils.formatPlaceholders(Gts.language.getInsufficientFunds(),
									0, listing.getListing().getDisplayName().getString(), listing.getSellerName(),
									action.getPlayer().getName().getString());
						}
					}
					action.getPlayer().sendSystemMessage(Component.literal(message));

					UIManager.closeUI(action.getPlayer());
				})
				.build();

		Button cancel = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getCancelButtonItem()))
				.title(Gts.language.getCancelPurchaseButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new AllListings().getPage();
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button removeListing = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getRemoveListingButtonItem()))
				.title(Gts.language.getRemoveListingButtonLabel())
				.onClick((action) -> {
					if (action.getPlayer().getUUID().equals(listing.getSellerUuid())) {
						GtsAPI.cancelAndReturnListing(action.getPlayer(), listing);
					} else {
						GtsAPI.cancelListing(listing);
					}
					String message = Utils.formatPlaceholders(Gts.language.getCancelListing(),
							0, listing.getListing().getDisplayName().getString(), listing.getSellerName(),
							action.getPlayer().getName().getString());
					action.getPlayer().sendSystemMessage(Component.literal(message));

					ServerPlayer seller =
							action.getPlayer().getServer().getPlayerList().getPlayer(listing.getSellerUuid());

					if (seller != null && !seller.getUUID().equals(action.getPlayer().getUUID())) {
						seller.sendSystemMessage(Component.literal(message));
					}

					UIManager.closeUI(action.getPlayer());
				})
				.build();

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getFillerItem()))
				.hideFlags(FlagType.All)
				.lore(new ArrayList<>())
				.title("")
				.build();

		ChestTemplate.Builder template = ChestTemplate.builder(3)
				.fill(filler)
				.set(13, pokemon)
				.set(15, cancel);

		if (viewer.getUUID().equals(listing.getSellerUuid())) {
			template.set(11, removeListing);
		} else {
			template.set(11, purchase);
		}

		if (Gts.permissions.hasPermission(viewer, Gts.permissions.getPermission("GtsMod"))
		&& !viewer.getUUID().equals(listing.getSellerUuid())) {
			template.set(22, removeListing);
		}

		GooeyPage page = GooeyPage.builder()
				.template(template.build())
				.title("§3" + Gts.language.getTitle() + " - Item")
				.build();

		return page;
	}
}
