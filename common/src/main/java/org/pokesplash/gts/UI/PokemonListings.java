package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.item.PokemonItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.PokemonListing;
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
			pkmListings.sort(Comparator.comparing(o -> o.getPokemon().getSpecies().toString()));
		}

		Button sortByPriceButton = GooeyButton.builder()
				.display(new ItemStack(Items.GOLD_NUGGET))
				.title("§eSort By Price")
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(SORT.PRICE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button sortByNewestButton = GooeyButton.builder()
				.display(new ItemStack(Items.CLOCK))
				.title("§3Sort By Newest")
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(SORT.DATE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button sortByNameButton = GooeyButton.builder()
				.display(new ItemStack(Items.OAK_SIGN))
				.title("§6Sort By Pokemon")
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(SORT.NAME);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button seeItemListings = GooeyButton.builder()
				.display(new ItemStack(CobblemonItems.ASSAULT_VEST.get()))
				.title("§9See Item Listings")
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ItemListings().getPage(ItemListings.SORT.NONE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button manageListings = GooeyButton.builder()
				.display(new ItemStack(CobblemonItems.SACHET.get()))
				.title("§dManage Listings")
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ManageListings().getPage(action.getPlayer().getUUID());
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		LinkedPageButton nextPage = LinkedPageButton.builder()
				.display(new ItemStack(Items.ARROW))
				.title("§7Next Page")
				.linkType(LinkType.Next)
				.build();

		LinkedPageButton previousPage = LinkedPageButton.builder()
				.display(new ItemStack(CobblemonItems.POISON_BARB.get()))
				.title("§7Previous Page")
				.linkType(LinkType.Previous)
				.build();


		PlaceholderButton placeholder = new PlaceholderButton();

		List<Button> pokemonButtons = new ArrayList<>();
		for (PokemonListing listing : Gts.listings.getPokemonListings()) {
			Collection<String> lore = new ArrayList<>();

			lore.add("§9Seller: §b" + listing.getSellerName());
			lore.add("§9Price: §b" + listing.getPrice());
			lore.add("§9Time Remaining: §b" + Utils.parseLongDate(listing.getEndTime() - new Date().getTime()));
			lore.addAll(PokemonInfo.parse(listing));

			Button button = GooeyButton.builder()
					.display(PokemonItem.from(listing.getPokemon(), 1))
					.title("§3" + Utils.capitaliseFirst(listing.getPokemon().getSpecies().toString()))
					.lore(lore)
					.onClick((action) -> {
						ServerPlayer sender = action.getPlayer();
						Page page = new SinglePokemonListing().getPage(sender, listing);
						UIManager.openUIForcefully(sender, page);
					})
					.build();
			pokemonButtons.add(button);
		}

		Button filler = GooeyButton.builder()
				.display(new ItemStack(Items.WHITE_STAINED_GLASS_PANE))
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
