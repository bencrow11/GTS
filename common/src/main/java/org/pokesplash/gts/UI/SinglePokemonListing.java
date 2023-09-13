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
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.module.PokemonInfo;
import org.pokesplash.gts.api.GtsAPI;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * UI of the Item Listings page.
 */
public class SinglePokemonListing {

	/**
	 * Method that returns the page.
	 * @return SinglePokemonListing page.
	 */
	public Page getPage(ServerPlayer viewer, PokemonListing listing) {

		Collection<String> lore = new ArrayList<>();

		lore.add("§9Seller: §b" + listing.getSellerName());
		lore.add("§9Price: §b" + listing.getPrice());
		lore.add("§9Time Remaining: §b" + Utils.parseLongDate(listing.getEndTime() - new Date().getTime()));
		lore.addAll(PokemonInfo.parse(listing));

		Button pokemon = GooeyButton.builder()
				.display(PokemonItem.from(listing.getPokemon(), 1))
				.title("§3" + Utils.capitaliseFirst(listing.getPokemon().getSpecies().toString()))
				.lore(lore)
				.build();

		Button purchase = GooeyButton.builder()
				.display(new ItemStack(Items.GREEN_STAINED_GLASS_PANE))
				.title("§2Confirm Purchase")
				.onClick((action) -> {
					boolean success = GtsAPI.sale(listing.getSellerUuid(), action.getPlayer().getUUID(), listing);

					String message;
					if (success) {
						message = Gts.language.getPurchase_pokemon_message_buyer().replaceAll("\\{pokemon\\}",
								listing.getPokemon().getSpecies().getName()).replaceAll("\\{seller\\}",
								listing.getSellerName()).replaceAll("\\{buyer}",
								action.getPlayer().getName().getString());

						ServerPlayer seller =
								action.getPlayer().getServer().getPlayerList().getPlayer(listing.getSellerUuid());

						if (seller != null) {
							seller.sendSystemMessage(Component.literal(Gts.language.getListing_bought_pokemon()
									.replaceAll("\\{pokemon\\}",
									listing.getPokemon().getSpecies().getName()).replaceAll("\\{seller\\}",
											listing.getSellerName()).replaceAll("\\{buyer}",
									action.getPlayer().getName().getString())));
						}

					} else {
						message = Gts.language.getInsufficient_funds().replaceAll("\\{pokemon\\}",
								listing.getPokemon().getSpecies().getName()).replaceAll("\\{seller\\}",
								action.getPlayer().getName().getString()).replaceAll("\\{buyer}",
								action.getPlayer().getName().getString());
					}
					action.getPlayer().sendSystemMessage(Component.literal(message));

					UIManager.closeUI(action.getPlayer());
				})
				.build();

		Button cancel = GooeyButton.builder()
				.display(new ItemStack(Items.RED_STAINED_GLASS_PANE))
				.title("§cCancel Purchase")
				.onClick((action) -> {
					ServerPlayer sender = action.getPlayer();
					Page page = new PokemonListings().getPage(PokemonListings.SORT.NONE);
					UIManager.openUIForcefully(sender, page);
				})
				.build();

		Button removeListing = GooeyButton.builder()
				.display(new ItemStack(Items.ORANGE_STAINED_GLASS_PANE))
				.title("§6Remove Listing")
				.onClick((action) -> {
					GtsAPI.cancelListing(listing);
					String message = Gts.language.getCancel_pokemon_listing().replaceAll("\\{pokemon\\}",
							listing.getPokemon().getSpecies().getName()).replaceAll("\\{seller\\}",
							action.getPlayer().getName().getString()).replaceAll("\\{buyer}",
							action.getPlayer().getName().getString());

					action.getPlayer().sendSystemMessage(Component.literal(message));

					ServerPlayer seller =
							action.getPlayer().getServer().getPlayerList().getPlayer(listing.getSellerUuid());

					if (seller != null && !seller.getUUID().equals(action.getPlayer().getUUID())) {
						seller.sendSystemMessage(Component.literal(message));
					}

					UIManager.closeUI(action.getPlayer());
				})
				.build();

		Button filler = GooeyButton.builder()
				.display(new ItemStack(Items.WHITE_STAINED_GLASS_PANE))
				.build();

		ChestTemplate.Builder template = ChestTemplate.builder(3)
				.fill(filler)
				.set(13, pokemon)
				.set(15, cancel);

		if (viewer.getUUID().equals(listing.getSellerUuid())) {
			template.set(11, removeListing);
		} else {
			template.set(11, purchase);
		}

		if (Gts.permissions.hasPermission(viewer, Gts.permissions.getPermission("GtsMod"))
		&& !viewer.getUUID().equals(listing.getSellerUuid())) {
			template.set(22, removeListing);
		}

		GooeyPage page = GooeyPage.builder()
				.template(template.build())
				.title("§3" + Gts.language.getTitle() + " - Pokemon")
				.build();

		return page;
	}
}
