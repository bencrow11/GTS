package org.pokesplash.gts;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.Permissions;

import java.util.UUID;

public class Gts
{
	public static final String MOD_ID = "gts";

	public static final Permissions permissions = new Permissions();

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		CommandRegistrationEvent.EVENT.register(CommandsRegistry::registerCommands);

		ListingsProvider listings = new ListingsProvider();
		listings.addItemListing(new ItemListing(UUID.randomUUID(), "bencrow11", 50000, 1, 123456L, Items.ITEM_FRAME));
		System.out.println(listings.getItemListings().toArray()[0]);

	}
}
