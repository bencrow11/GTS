package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.api.GtsAPI;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * UI of the Item Listings page.
 */
public class SinglePokemonListing {

	/**
	 * Method that returns the page.
	 * @return SinglePokemonListing page.
	 */
	public Page getPage(ServerPlayer viewer, PokemonListing listing) {



		Collection<Component> lore = new ArrayList<>();

		lore.add(Component.literal(Gts.language.getSeller() + listing.getSellerName()));
		lore.add(Component.literal(Gts.language.getPrice() + listing.getPriceAsString()));
		lore.add(Component.literal(Gts.language.getTime_remaining() + Utils.parseLongDate(listing.getEndTime() - new Date().getTime())));
		lore.addAll(PokemonInfo.parse(listing));

		Button pokemon = GooeyButton.builder()
				.display(PokemonItem.from(listing.getListing(), 1))
				.title(listing.getDisplayName())
				.lore(Component.class, lore)
				.build();

		Button purchase = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getPurchase_button()))
				.title(Gts.language.getConfirm_purchase())
				.onClick((action) -> {
					boolean success = GtsAPI.sale(listing.getSellerUuid(), action.getPlayer(), listing);

					String message;
					if (success) {
						message = Utils.formatPlaceholders(Gts.language.getPurchase_message_buyer(),
								0, listing.getListing().getDisplayName().getString(), listing.getSellerName(),
								action.getPlayer().getName().getString());

						ServerPlayer seller =
								action.getPlayer().getServer().getPlayerList().getPlayer(listing.getSellerUuid());

						if (seller != null) {
							seller.sendSystemMessage(Component.literal(Utils.formatPlaceholders(Gts.language.getListing_bought(),
									0, listing.getListing().getDisplayName().getString(), listing.getSellerName(),
									action.getPlayer().getName().getString())));
						}

					} else {
						message = Utils.formatPlaceholders(Gts.language.getInsufficient_funds(),
								0, listing.getListing().getDisplayName().getString(), listing.getSellerName(),
								action.getPlayer().getName().getString());
					}
					action.getPlayer().sendSystemMessage(Component.literal(message));

					UIManager.closeUI(action.getPlayer());
				})
				.build();

		Button cancel = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getCancel_button()))
				.title(Gts.language.getCancel_purchase())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new AllListings().getPage();
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button removeListing = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getRemove_listing_button()))
				.title(Gts.language.getRemove_listing())
				.onClick((action) -> {


					if (action.getPlayer().getUUID().equals(listing.getSellerUuid())) {
						GtsAPI.cancelAndReturnListing(action.getPlayer(), listing);
					} else {
						GtsAPI.cancelListing(listing);
					}
					String message = Utils.formatPlaceholders(Gts.language.getCancel_listing(),
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
				.display(Utils.parseItemId(Gts.language.getFiller_item()))
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
				.title("§3" + Gts.language.getTitle() + " - Pokemon")
				.build();

		return page;
	}
}
