package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemLore;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.UI.button.Filler;
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.api.GtsAPI;
import org.pokesplash.gts.util.Utils;

import java.util.Collection;
import java.util.List;

/**
 * UI of the Single Item Listing
 */
public class SingleItemListing {

	/**
	 * Method that returns the page.
	 * @return SinglePokemonListing page.
	 */
	public Page getPage(ServerPlayer viewer, ItemListing listing) {

		List<Component> lore = ListingInfo.parse(listing);

		Button pokemon = GooeyButton.builder()
				.display(listing.getListing())
				.with(DataComponents.CUSTOM_NAME,
						Component.literal("§3" + Utils.capitaliseFirst(listing.getListingName())))
				.with(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
				.with(DataComponents.LORE, new ItemLore(lore))
				.build();

		Button purchase = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getPurchaseButtonItem()))
				.with(DataComponents.CUSTOM_NAME,
						Component.literal(Gts.language.getConfirmPurchaseButtonLabel()))
				.onClick((action) -> {
					String message;

				  if (Gts.listings.getActiveListingById(listing.getId()) == null) {
						message = "§cThis item is no longer available.";
					} else if (!Utils.hasSpace(action.getPlayer(), listing.getListing()))
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
				.with(DataComponents.CUSTOM_NAME,
						Component.literal(Gts.language.getCancelPurchaseButtonLabel()))
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new AllListings().getPage();
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button removeListing = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getRemoveListingButtonItem()))
				.with(DataComponents.CUSTOM_NAME,
						Component.literal(Gts.language.getRemoveListingButtonLabel()))
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

		ChestTemplate.Builder template = ChestTemplate.builder(3)
				.fill(Filler.getButton())
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
				.title(Gts.language.getItemTitle())
				.build();

		return page;
	}
}
