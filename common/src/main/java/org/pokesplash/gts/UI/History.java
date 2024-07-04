package org.pokesplash.gts.UI;

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
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.UI.button.ManageListings;
import org.pokesplash.gts.UI.button.*;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.history.HistoryItem;
import org.pokesplash.gts.history.ItemHistoryItem;
import org.pokesplash.gts.history.PlayerHistory;
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

		PlaceholderButton placeholder = new PlaceholderButton();

		PlayerHistory playerHistory = Gts.history.getPlayerHistory(owner);

		List<Button> buttons = new ArrayList<>();

		if (playerHistory != null) {
			// Gets all the items and sorts them by the sold date.
			List<HistoryItem> items = playerHistory.getListings();
			items.sort(Comparator.comparing(HistoryItem::getSoldDate));
			Collections.reverse(items);

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
							.hideFlags(FlagType.All)
							.build();
				}

				// Adds the button to the list.
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
