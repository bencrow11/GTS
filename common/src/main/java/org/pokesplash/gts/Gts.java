package org.pokesplash.gts;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.history.HistoryProvider;
import org.pokesplash.gts.history.PlayerHistory;
import org.pokesplash.gts.timer.TimerProvider;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.GtsLogger;
import org.pokesplash.gts.util.Permissions;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Gts
{
	public static final String MOD_ID = "gts";
	public static final Permissions permissions = new Permissions();
	public static ListingsProvider listings = new ListingsProvider();
	public static HistoryProvider history = new HistoryProvider();
	public static TimerProvider timers = new TimerProvider();
	public static final GtsLogger LOGGER = new GtsLogger();

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		CommandRegistrationEvent.EVENT.register(CommandsRegistry::registerCommands);
		listings.init();
		history.init();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				LOGGER.error("Test 3");
				LOGGER.info("Test 4");
			}
		}, 1000 * 10);
	}
}
