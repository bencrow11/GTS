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
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.api.provider.ListingAPI;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * UI of the filtered Pokemon Listings page.
 */
public class FilteredListings {

	/**
	 * Method that returns the page.
	 * @return Pokemon Listings page.
	 */
	public Page getPage(String searchValue) {


		PlaceholderButton placeholder = new PlaceholderButton();

		List<Button> buttons = new ArrayList<>();

		List<Listing> listings = ListingAPI.getHighestPriority() == null ? Gts.listings.getListings() :
				Gts.listings.getListings().stream().map(Listing::deepClone).toList();

		for (Listing listing : listings) {

			if (listing.getListingName().toLowerCase().contains(searchValue.toLowerCase())
				|| listing.getSellerName().toLowerCase().contains(searchValue.toLowerCase())) {
				Collection<Component> lore = ListingInfo.parse(listing);

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
								Page page = new SinglePokemonListing().getPage(sender, pokemonListing);
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
								Page page = new SingleItemListing().getPage(sender, itemListing);
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
		page.setTitle(Gts.language.getFilteredListingsTitle().replaceAll("%search%", searchValue));

		setPageTitle(page, searchValue);

		return page;
	}

	private void setPageTitle(LinkedPage page, String searchValue) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle(Gts.language.getFilteredListingsTitle().replaceAll("%search%", searchValue));
			setPageTitle(next, searchValue);
		}
	}
}
