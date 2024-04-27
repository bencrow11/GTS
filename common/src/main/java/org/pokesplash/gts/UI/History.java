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
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.history.HistoryItem;
import org.pokesplash.gts.history.ItemHistoryItem;
import org.pokesplash.gts.history.PokemonHistoryItem;
import org.pokesplash.gts.util.Utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * UI of the Manage Listings page.
 */
public class History {

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

		Button manageListings = GooeyButton.builder()
				.display(Utils.parseItemId(Gts.language.getManageListingsButtonItem()))
				.title(Gts.language.getManageListingsButtonLabel())
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ManageListings().getPage(action.getPlayer().getUUID());
					UIManager.openUIForcefully(sender, page);
				})
				.build();


		PlaceholderButton placeholder = new PlaceholderButton();

		// Gets all the items and sorts them by the sold date.
		List<HistoryItem> items = Gts.history.getPlayerHistory(owner).getListings();
		items.sort(Comparator.comparing(HistoryItem::getSoldDate));
		Collections.reverse(items);

		List<Button> buttons = new ArrayList<>();

		// For each item, create a button.
		for (HistoryItem item : items) {

			// Standard lore for any item.
			Collection<Component> lore = new ArrayList<>();
			lore.add(Component.literal(Gts.language.getSeller() + item.getSellerName()));
			lore.add(Component.literal(Gts.language.getPrice() + item.getPriceAsString()));
			lore.add(Component.literal(Gts.language.getBuyer() + item.getBuyerName()));

			String pattern = "d MMMM yyyy";
			SimpleDateFormat format = new SimpleDateFormat(pattern);

			lore.add(Component.literal(Gts.language.getSold_date() +
					format.format(new Date(item.getSoldDate()))));

			Button button;

			// Pokemon specific lore and button.
			if (item.isPokemon()) {
				PokemonHistoryItem pokemonItem = (PokemonHistoryItem) item;
				lore.addAll(PokemonInfo.parse(pokemonItem.getListing()));

				button = GooeyButton.builder()
						.display(PokemonItem.from(pokemonItem.getListing(), 1))
						.title(pokemonItem.getListing().getDisplayName())
						.lore(Component.class, lore)
						.build();
			}
			// Item specific button.
			else {
				ItemHistoryItem itemHistoryItem = (ItemHistoryItem) item;

				button = GooeyButton.builder()
						.display(itemHistoryItem.getListing())
						.title("§3" + Utils.capitaliseFirst(
								itemHistoryItem.getListing().getDisplayName().getString()))
						.lore(Component.class, lore)
						.build();
			}

			// Adds the button to the list.
			buttons.add(button);
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
				.set(48, seePokemonListings)
				.set(49, manageListings)
				.set(50, seeItemListings)
				.set(53, nextPage)
				.set(45, previousPage)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, buttons, null);
		page.setTitle("§3" + Gts.language.getTitle() + " - History");

		setPageTitle(page);

		return page;
	}

	private void setPageTitle(LinkedPage page) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle("§3" + Gts.language.getTitle() + " - History");
			setPageTitle(next);
		}
	}
}
