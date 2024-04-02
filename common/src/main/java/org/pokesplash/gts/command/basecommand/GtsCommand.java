package org.pokesplash.gts.command.basecommand;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.page.Page;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.UI.AllListings;
import org.pokesplash.gts.command.subcommand.*;
import org.pokesplash.gts.util.BaseCommand;

import java.util.Arrays;

/**
 * Creates the mods base command.
 */
public class GtsCommand extends BaseCommand {

	public GtsCommand() {
		super("gts", Arrays.asList("gts"),
				Gts.permissions.getPermission("GtsCommand"),
				Arrays.asList(new Manage(), new Expired(), new List(), new History(),
						new Reload(), new Open(), new Debug()));
	}

	// Runs when the base command is run with no subcommands.
	@Override
	public int run(CommandContext<CommandSourceStack> context) {


		if (!context.getSource().isPlayer()) {
			context.getSource().sendSystemMessage(Component.literal("This command must be ran by a player."));
			return 1;
		}

		ServerPlayer sender = context.getSource().getPlayer();

		Page page = new AllListings().getPage();

		UIManager.openUIForcefully(sender, page);

		return 1;
	}
}
