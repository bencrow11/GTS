package org.pokesplash.gts;

import dev.architectury.event.Event;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.config.Config;
import org.pokesplash.gts.config.Lang;
import org.pokesplash.gts.history.HistoryProvider;
import org.pokesplash.gts.timer.TimerProvider;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.GtsLogger;
import org.pokesplash.gts.util.Permissions;

import java.util.Timer;
import java.util.TimerTask;

public class Gts
{
	public static final String MOD_ID = "gts";
	public static Config config;
	public static final Permissions permissions = new Permissions();
	public static ListingsProvider listings;
	public static HistoryProvider history;
	public static TimerProvider timers = new TimerProvider();
	public static final GtsLogger LOGGER = new GtsLogger();
	public static Lang language;

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		CommandRegistrationEvent.EVENT.register(CommandsRegistry::registerCommands);
		reload();
	}

	public static void reload() {
		config = new Config();
		listings = new ListingsProvider();
		history = new HistoryProvider();
		language = new Lang();
		config.init();
		listings.init();
		history.init();
		language.init();
	}

}
