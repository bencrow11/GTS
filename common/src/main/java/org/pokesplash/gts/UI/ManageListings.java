package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.component.ItemLore;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.button.ExpiredListings;
import org.pokesplash.gts.UI.button.*;
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.UI.module.PokemonInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * UI of the Manage Listings page.
 */
public class ManageListings {

	/**
	 * Method that returns the page.
	 * @return Listings page.
	 */
	public Page getPage(UUID owner) {

		List<Listing> listings = Gts.listings.getListingsByPlayer(owner);

		PlaceholderButton placeholder = new PlaceholderButton();

		List<Button> buttons = new ArrayList<>();
		if (!listings.isEmpty()) {
			listings.forEach(listing -> {
				List<Component> lore = ListingInfo.parse(listing);

				if (listing.isPokemon()) {
					PokemonListing pokemonListing = (PokemonListing) listing;
					lore.addAll(PokemonInfo.parse(pokemonListing.getListing()));
				}

				Button button = GooeyButton.builder()
						.display(listing.getIcon())
						.with(DataComponents.CUSTOM_NAME, listing.getDisplayName())
						.with(DataComponents.LORE, new ItemLore(lore))
						.onClick((action) -> {
							ServerPlayer sender = action.getPlayer();
							Page page = new SingleListing().getPage(sender, listing);
							UIManager.openUIForcefully(sender, page);
						})
						.build();

				buttons.add(button);
			});
		}

		ChestTemplate template = ChestTemplate.builder(6)
				.rectangle(0, 0, 5, 9, placeholder)
				.fill(Filler.getButton())
				.set(48, SeePokemonListings.getButton())
				.set(49, ExpiredListings.getButton())
				.set(50, SeeItemListings.getButton())
				.set(53, NextPage.getButton())
				.set(45, PreviousPage.getButton())
				.set(52, RelistAll.getButton())
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
		page.setTitle(Gts.language.getManageTitle());

		setPageTitle(page);

		return page;
	}

	private void setPageTitle(LinkedPage page) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle(Gts.language.getManageTitle());
			setPageTitle(next);
		}
	}
}
