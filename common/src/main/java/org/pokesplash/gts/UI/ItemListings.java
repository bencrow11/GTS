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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * UI of the Item Listings page.
 */
public class ItemListings {

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

		List<ItemListing> itmListings = Gts.listings.getItemListings();

		if (sort.equals(SORT.PRICE)) {
			itmListings.sort(Comparator.comparingDouble(ItemListing::getPrice));
		} else if (sort.equals(SORT.DATE)) {
			itmListings.sort(Comparator.comparingLong(ItemListing::getEndTime));
		} else if (sort.equals(SORT.NAME)) {
			itmListings.sort(Comparator.comparing(o -> o.getListing().getDisplayName().getString()));
		}

		Button sortByPriceButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByPriceButtonItem()))
				.title(Gts.language.getSortByPriceButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ItemListings().getPage(SORT.PRICE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button sortByNewestButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByNewestButtonItem()))
				.title(Gts.language.getSortByNewestButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ItemListings().getPage(SORT.DATE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button sortByNameButton = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getSortByNameButtonItem()))
				.title(Gts.language.getSortByNameButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ItemListings().getPage(SORT.NAME);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button seePokemonListings = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getPokemonListingsButtonItem()))
				.hideFlags(FlagType.All)
				.title(Gts.language.getPokemonListingsButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(PokemonListings.SORT.NONE);
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

		List<Button> itemButtons = new ArrayList<>();
		for (ItemListing listing : Gts.listings.getItemListings()) {
			Collection<Component> lore = ListingInfo.parse(listing);

			Button button = GooeyButton.builder()
					.display(listing.getListing())
					.title("§3" + Utils.capitaliseFirst(listing.getListing().getDisplayName().getString()))
					.lore(Component.class, lore)
					.onClick((action) -> {
						ServerPlayer sender = action.getPlayer();
						Page page = new SingleItemListing().getPage(sender, listing);
						UIManager.openUIForcefully(sender, page);
					})
					.build();
			itemButtons.add(button);
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
				.set(50, seePokemonListings)
				.set(51, manageListings)
				.set(53, nextPage)
				.set(45, previousPage)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, itemButtons, null);
		page.setTitle("§3" + Gts.language.getTitle() + " - Items");

		setPageTitle(page);

		return page;
	}

	private void setPageTitle(LinkedPage page) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle("§3" + Gts.language.getTitle() + " - Items");
			setPageTitle(next);
		}
	}
}
