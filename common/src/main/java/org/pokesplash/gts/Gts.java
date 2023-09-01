package org.pokesplash.gts;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.Permissions;

import java.util.Timer;
import java.util.TimerTask;

public class Gts
{
	public static final String MOD_ID = "gts";
	public static final Permissions permissions = new Permissions();
	public static ListingsProvider listings = new ListingsProvider();
	public static final Logger LOGGER = LogManager.getLogger();

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		CommandRegistrationEvent.EVENT.register(CommandsRegistry::registerCommands);
		listings.initialize();
	}
}
