package org.pokesplash.gts;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.Permissions;

public class Gts
{
	public static final String MOD_ID = "gts";

	public static final Permissions permissions = new Permissions();

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		CommandRegistrationEvent.EVENT.register(CommandsRegistry::registerCommands);
	}
}
