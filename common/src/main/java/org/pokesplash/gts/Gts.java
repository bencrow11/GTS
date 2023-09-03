package org.pokesplash.gts;

import com.google.gson.Gson;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.history.HistoryProvider;
import org.pokesplash.gts.history.PlayerHistory;
import org.pokesplash.gts.timer.TimerProvider;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.ImpactorService;
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

	public static ImpactorService impactor = new ImpactorService(null);
	public static final Logger LOGGER = LogManager.getLogger();

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		CommandRegistrationEvent.EVENT.register(CommandsRegistry::registerCommands);
		listings.init();
		history.init();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				PlayerHistory ben = new PlayerHistory(UUID.fromString("b5c833a0-c6f7-4e89-9ad5-d36faef37ab2"));
			}
		}, 1000 * 10);
	}
}
