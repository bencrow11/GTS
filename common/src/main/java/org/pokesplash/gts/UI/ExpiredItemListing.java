package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.api.GtsAPI;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * UI of the Expired Item Listing page.
 */
public class ExpiredItemListing {

	/**
	 * Method that returns the page.
	 * @return SinglePokemonListing page.
	 */
	public Page getPage(ItemListing listing) {

		Collection<String> lore = new ArrayList<>();

		lore.add("§9Seller: §b" + listing.getSellerName());
		lore.add("§9Price: §b" + listing.getPrice());
		lore.add("§9Time Remaining: §b" + Utils.parseLongDate(listing.getEndTime() - new Date().getTime()));

		Button pokemon = GooeyButton.builder()
				.display(new ItemStack(listing.getItem()))
				.title("§3" + Utils.capitaliseFirst(new ItemStack(listing.getItem()).getDisplayName().getString()))
				.lore(lore)
				.build();

		Button receiveListing = GooeyButton.builder()
				.display(new ItemStack(Items.GREEN_STAINED_GLASS_PANE))
				.title("§2Receive Listing")
				.onClick((action) -> {
					UIManager.closeUI(action.getPlayer());
					GtsAPI.returnListing(action.getPlayer(), listing);

					String message = Gts.language.getReturn_item_listing().replaceAll("\\{item\\}",
							Utils.capitaliseFirst(new ItemStack(listing.getItem()).getDisplayName().getString())).replaceAll("\\{seller\\}",
							action.getPlayer().getName().getString()).replaceAll("\\{buyer}",
							action.getPlayer().getName().getString());
					action.getPlayer().sendSystemMessage(Component.literal(message));
				})
				.build();

		Button cancel = GooeyButton.builder()
				.display(new ItemStack(Items.RED_STAINED_GLASS_PANE))
				.title("§cCancel Purchase")
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new ExpiredListings().getPage(action.getPlayer().getUUID());
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button filler = GooeyButton.builder()
				.display(new ItemStack(Items.WHITE_STAINED_GLASS_PANE))
				.build();

		ChestTemplate.Builder template = ChestTemplate.builder(3)
				.fill(filler)
				.set(11, receiveListing)
				.set(13, pokemon)
				.set(15, cancel);


		GooeyPage page = GooeyPage.builder()
				.template(template.build())
				.title("§3" + Gts.language.getTitle() + " - Pokemon")
				.build();

		return page;
	}
}
