package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.button.ManageListings;
import org.pokesplash.gts.UI.button.*;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * UI of the Manage Listings page.
 */
public class ExpiredListings {

	/**
	 * Method that returns the page.
	 * @return Pokemon Listings page.
	 */
	public Page getPage(UUID owner) {

		PlaceholderButton placeholder = new PlaceholderButton();

		List<Listing> listings = Gts.listings.getExpiredListingsOfPlayer(owner);

		List<Button> buttons = new ArrayList<>();
		if (listings != null) {
			for (Listing listing : listings) {


				Collection<Component> lore = new ArrayList<>();
				lore.add(Component.literal(Gts.language.getSeller() + listing.getSellerName()));
				lore.add(Component.literal(Gts.language.getPrice() + listing.getPriceAsString()));

				Button button;

				if (listing instanceof PokemonListing) {

					PokemonListing pokemonListing = (PokemonListing) listing;

					lore.addAll(PokemonInfo.parse(pokemonListing));

					button = GooeyButton.builder()
							.display(PokemonItem.from(pokemonListing.getListing(), 1))
							.title(pokemonListing.getDisplayName())
							.lore(Component.class, lore)
							.onClick((action) -> {
								ServerPlayer sender = action.getPlayer();
								Page page = new ExpiredPokemonListing().getPage(pokemonListing);
								UIManager.openUIForcefully(sender, page);
							})
							.build();
				} else {
					ItemListing itemListing = (ItemListing) listing;

					button = GooeyButton.builder()
							.display(itemListing.getListing())
							.title("§3" + Utils.capitaliseFirst(itemListing.getListingName()))
							.lore(Component.class, lore)
							.hideFlags(FlagType.All)
							.onClick((action) -> {
								ServerPlayer sender = action.getPlayer();
								Page page = new ExpiredItemListing().getPage(itemListing);
								UIManager.openUIForcefully(sender, page);
							})
							.build();
				}

				buttons.add(button);
			}
		}

		ChestTemplate template = ChestTemplate.builder(6)
				.rectangle(0, 0, 5, 9, placeholder)
				.fill(Filler.getButton())
				.set(48, SeePokemonListings.getButton())
				.set(49, ManageListings.getButton())
				.set(50, SeeItemListings.getButton())
				.set(53, NextPage.getButton())
				.set(45, PreviousPage.getButton())
				.set(52, RelistAll.getButton())
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
		page.setTitle(Gts.language.getExpiredListingsTitle());

		setPageTitle(page);

		return page;
	}

	private void setPageTitle(LinkedPage page) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle(Gts.language.getExpiredListingsTitle());
			setPageTitle(next);
		}
	}
}
