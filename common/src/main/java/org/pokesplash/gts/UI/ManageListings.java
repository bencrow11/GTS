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
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.button.ExpiredListings;
import org.pokesplash.gts.UI.button.*;
import org.pokesplash.gts.UI.module.ListingInfo;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * UI of the Manage Listings page.
 */
public class ManageListings {

	/**
	 * Method that returns the page.
	 * @return Pokemon Listings page.
	 */
	public Page getPage(UUID owner) {

		List<PokemonListing> pkmListings = Gts.listings.getPokemonListingsByPlayer(owner);
		List<ItemListing> itmListings = Gts.listings.getItemListingsByPlayer(owner);

		PlaceholderButton placeholder = new PlaceholderButton();

		List<Button> pokemonButtons = new ArrayList<>();
		if (pkmListings != null) {
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
		}

		List<Button> itemButtons = new ArrayList<>();
		if (itmListings != null) {
			for (ItemListing listing : itmListings) {
				Collection<Component> lore = ListingInfo.parse(listing);

				Button button = GooeyButton.builder()
						.display(listing.getListing())
						.title("§3" + Utils.capitaliseFirst(listing.getListingName()))
						.lore(Component.class, lore)
						.hideFlags(FlagType.All)
						.onClick((action) -> {
							ServerPlayer sender = action.getPlayer();
							Page page = new SingleItemListing().getPage(sender, listing);
							UIManager.openUIForcefully(sender, page);
						})
						.build();
				itemButtons.add(button);
			}
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

		pokemonButtons.addAll(itemButtons);

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, pokemonButtons, null);
		page.setTitle("§3" + Gts.language.getTitle() + " - Manage");

		setPageTitle(page);

		return page;
	}

	private void setPageTitle(LinkedPage page) {
		LinkedPage next = page.getNext();
		if (next != null) {
			next.setTitle("§3" + Gts.language.getTitle() + " - Manage");
			setPageTitle(next);
		}
	}
}
