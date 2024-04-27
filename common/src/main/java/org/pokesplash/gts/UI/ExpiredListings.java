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
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.util.Utils;

import java.util.*;

/**
 * UI of the Manage Listings page.
 */
public class ExpiredListings {

	/**
	 * Method that returns the page.
	 * @return Pokemon Listings page.
	 */
	public Page getPage(UUID owner) {

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
							.title("§3" + Utils.capitaliseFirst(itemListing.getListing().getDisplayName().getString()))
							.lore(Component.class, lore)
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

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getFillerItem()))
				.hideFlags(FlagType.All)
				.lore(new ArrayList<>())
				.title("")
				.build();

		Button relistAll = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getRelistExpiredButtonItem()))
				.title(Gts.language.getRelistExpiredButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Gts.listings.relistAllExpiredListings(sender.getUUID());
					Page page = new ExpiredListings().getPage(sender.getUUID());
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		ChestTemplate template = ChestTemplate.builder(6)
				.rectangle(0, 0, 5, 9, placeholder)
				.fill(filler)
				.set(48, seePokemonListings)
				.set(49, manageListings)
				.set(50, seeItemListings)
				.set(53, nextPage)
				.set(45, previousPage)
				.set(52, relistAll)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
		page.setTitle("§3" + Gts.language.getTitle() + " - Expired");

		setPageTitle(page);

		return page;
	}

	private void setPageTitle(LinkedPage page) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle("§3" + Gts.language.getTitle() + " - Expired");
			setPageTitle(next);
		}
	}
}
