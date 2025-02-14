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
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemLore;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.button.Filler;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.api.GtsAPI;
import org.pokesplash.gts.util.ColorUtil;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * UI of the Expired Listing page.
 */
public class ExpiredListing {

	/**
	 * Method that returns the page.
	 * @return SingleListing page.
	 */
	public Page getPage(Listing listing) {

		List<Component> lore = new ArrayList<>();

		lore.add(ColorUtil.parse(Gts.language.getSeller() + listing.getSellerName()));
		lore.add(ColorUtil.parse(Gts.language.getPrice() + listing.getPriceAsString()));

		if (listing.isPokemon()) {
			lore.addAll(PokemonInfo.parse((PokemonListing) listing));
		}

		Button listingDisplay = GooeyButton.builder()
				.display(listing.getIcon())
				.with(DataComponents.CUSTOM_NAME, listing.getDisplayName())
				.with(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
				.with(DataComponents.LORE, new ItemLore(lore))
				.build();

		Button receiveListing = GooeyButton.builder()
				.display(Gts.language.getPurchaseButtonItem())
				.with(DataComponents.CUSTOM_NAME,
						ColorUtil.parse(Gts.language.getReceiveListingButtonLabel()))
				.onClick((action) -> {
					boolean success = GtsAPI.returnListing(action.getPlayer(), listing);

					String message = "";

					if (success) {
						message = Utils.formatPlaceholders(Gts.language.getReturnListingSuccess(),
								0, listing.getListingName(), listing.getSellerName(),
								action.getPlayer().getName().getString());
						action.getPlayer().sendSystemMessage(ColorUtil.parse(message));
					} else {
						message = Utils.formatPlaceholders(Gts.language.getReturnListingFail(),
								0, listing.getListingName(), listing.getSellerName(),
								action.getPlayer().getName().getString());
						action.getPlayer().sendSystemMessage(ColorUtil.parse(message));
					}

					UIManager.openUIForcefully(action.getPlayer(), new ExpiredListings().getPage(action.getPlayer().getUUID()));
				})
				.build();

		Button cancel = GooeyButton.builder()
				.display(Gts.language.getCancelButtonItem())
				.with(DataComponents.CUSTOM_NAME,
						ColorUtil.parse(Gts.language.getCancelPurchaseButtonLabel()))
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ExpiredListings().getPage(action.getPlayer().getUUID());
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		ChestTemplate.Builder template = ChestTemplate.builder(3)
				.fill(Filler.getButton())
				.set(11, receiveListing)
				.set(13, listingDisplay)
				.set(15, cancel);


		GooeyPage page = GooeyPage.builder()
				.template(template.build())
				.title(listing.getUiTitle())
				.build();

		return page;
	}
}
