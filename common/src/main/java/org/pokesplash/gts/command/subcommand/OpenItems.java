package org.pokesplash.gts.command.subcommand;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.page.Page;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.UI.AllListings;
import org.pokesplash.gts.UI.ItemListings;
import org.pokesplash.gts.UI.SingleListing;
import org.pokesplash.gts.command.superclass.Subcommand;
import org.pokesplash.gts.enumeration.Sort;

import java.util.UUID;

public class OpenItems extends Subcommand {

	public OpenItems() {
		super("§9Usage:\n§3- gts item");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 *
	 * @return source to complete the command.
	 */
	@Override
	public CommandNode<CommandSourceStack> build() {
        return Commands.literal("item")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Gts.permissions.hasPermission(ctx.getPlayer(),
								"base");
					} else {
						return true;
					}
				})
				.executes(this::run)
				.build();
	}

	/**
	 * Method to perform the logic when the command is executed.
	 * @param context the source of the command.
	 * @return integer to complete command.
	 */
	@Override
	public int run(CommandContext<CommandSourceStack> context) {

		if (!context.getSource().isPlayer()) {
			context.getSource().sendSystemMessage(Component.literal("This command must be ran by a player."));
			return 1;
		}

		ServerPlayer player = context.getSource().getPlayer();

        Page page = new ItemListings().getPage(Sort.NONE);

        UIManager.openUIForcefully(player, page);
		return 1;
	}
}
