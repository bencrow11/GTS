package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.util.Utils;

import java.util.*;

/**
 * UI of the Pokemon Listings page.
 */
public class PokemonListings {

	public enum SORT {
		DATE,
		NAME,
		PRICE,
		NONE
	}

	/**
	 * Method that returns the page.
	 * @return Pokemon Listings page.
	 */
	public Page getPage(SORT sort) {

		List<PokemonListing> pkmListings = Gts.listings.getPokemonListings();

		if (sort.equals(SORT.PRICE)) {
			pkmListings.sort(Comparator.comparingDouble(PokemonListing::getPrice));
		} else if (sort.equals(SORT.DATE)) {
			pkmListings.sort(Comparator.comparingLong(PokemonListing::getEndTime));
		} else if (sort.equals(SORT.NAME)) {
			pkmListings.sort(Comparator.comparing(o -> o.getListing().getSpecies().toString()));
		}

		Button sortByPriceButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByPriceButtonItem()))
				.title(Gts.language.getSortByPriceButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(SORT.PRICE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button sortByNewestButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByNewestButtonItem()))
				.title(Gts.language.getSortByNewestButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(SORT.DATE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button sortByNameButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByNameButtonItem()))
				.title(Gts.language.getSortByPokemonButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(SORT.NAME);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button seeItemListings = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getItemListingsButtonItem()))
				.title(Gts.language.getItemListingsButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ItemListings().getPage(ItemListings.SORT.NONE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button manageListings = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getManageListingsButtonItem()))
				.title(Gts.language.getManageListingsButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ManageListings().getPage(action.getPlayer().getUUID());
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		LinkedPageButton nextPage = LinkedPageButton.builder()
				.display(Utils.parseItemId(Gts.language.getNextPageButtonItems()))
				.title(Gts.language.getNextPageButtonLabel())
				.linkType(LinkType.Next)
				.build();

		LinkedPageButton previousPage = LinkedPageButton.builder()
				.display(Utils.parseItemId(Gts.language.getPreviousPageButtonItems()))
				.title(Gts.language.getPreviousPageButtonLabel())
				.linkType(LinkType.Previous)
				.build();


		PlaceholderButton placeholder = new PlaceholderButton();

		List<Button> pokemonButtons = new ArrayList<>();
		for (PokemonListing listing : Gts.listings.getPokemonListings()) {
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

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getFillerItem()))
				.hideFlags(FlagType.All)
				.lore(new ArrayList<>())
				.title("")
				.build();

		ChestTemplate template = ChestTemplate.builder(6)
				.rectangle(0, 0, 5, 9, placeholder)
				.fill(filler)
				.set(47, sortByPriceButton)
				.set(48, sortByNewestButton)
				.set(49, sortByNameButton)
				.set(50, seeItemListings)
				.set(51, manageListings)
				.set(53, nextPage)
				.set(45, previousPage)
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
