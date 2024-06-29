package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
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
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.button.ManageListings;
import org.pokesplash.gts.UI.button.*;
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.enumeration.Sort;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * UI of the Pokemon Listings page.
 */
public class PokemonListings {

	/**
	 * Method that returns the page.
	 * @return Pokemon Listings page.
	 */
	public Page getPage(Sort sort) {

		List<PokemonListing> pkmListings = Gts.listings.getPokemonListings();

		if (sort.equals(Sort.PRICE)) {
			pkmListings.sort(Comparator.comparingDouble(PokemonListing::getPrice));
		} else if (sort.equals(Sort.DATE)) {
			pkmListings.sort(Comparator.comparingLong(PokemonListing::getEndTime));
		} else if (sort.equals(Sort.NAME)) {
			pkmListings.sort(Comparator.comparing(PokemonListing::getListingName));
		}

		Button sortByPriceButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByPriceButtonItem()))
				.title(Gts.language.getSortByPriceButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(Sort.PRICE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button sortByNewestButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByNewestButtonItem()))
				.title(Gts.language.getSortByNewestButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(Sort.DATE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button sortByNameButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByNameButtonItem()))
				.title(Gts.language.getSortByPokemonButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(Sort.NAME);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		PlaceholderButton placeholder = new PlaceholderButton();

		List<Button> pokemonButtons = new ArrayList<>();
		for (PokemonListing listing : pkmListings) {
			Collection<Component> lore = ListingInfo.parse(listing);
			lore.addAll(PokemonInfo.parse(listing));

			Button button = GooeyButton.builder()
					.display(PokemonItem.from(listing.getListing(), 1))
					.title(listing.getDisplayName())
					.lore(Component.class, lore)
					.onClick((action) -> {
						ServerPlayer sender = action.getPlayer();
						Page page = new SinglePokemonListing().getPage(sender, listing);
						UIManager.openUIForcefully(sender, page);
					})
					.build();
			pokemonButtons.add(button);
		}

		ChestTemplate template = ChestTemplate.builder(6)
				.rectangle(0, 0, 5, 9, placeholder)
				.fill(Filler.getButton())
				.set(47, sortByPriceButton)
				.set(48, sortByNewestButton)
				.set(49, sortByNameButton)
				.set(50, SeeItemListings.getButton())
				.set(51, ManageListings.getButton())
				.set(53, NextPage.getButton())
				.set(45, PreviousPage.getButton())
				.set(52, RelistAll.getButton())
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, pokemonButtons, null);
		page.setTitle("§3" + Gts.language.getTitle() + " - Pokemon");

		setPageTitle(page);

		return page;
	}

	private void setPageTitle(LinkedPage page) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle("§3" + Gts.language.getTitle() + " - Pokemon");
			setPageTitle(next);
		}
	}
}
