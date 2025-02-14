package org.pokesplash.gts.command.subcommand;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.page.Page;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.UI.FilteredListings;
import org.pokesplash.gts.command.superclass.Subcommand;

public class Search extends Subcommand {

	public Search() {
		super("§9Usage:\n§3- gts search pokemon/item");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("search")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Gts.permissions.hasPermission(ctx.getPlayer(),
								"search");
					} else {
						return true;
					}
				})
				.executes(this::showUsage)
					.then(Commands.argument("value", StringArgumentType.greedyString())
					.executes(this::run))
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
			context.getSource().sendSystemMessage(Component.literal(
					"This command must be ran by a player."
			));
			return 1;
		}

		ServerPlayer sender = context.getSource().getPlayer();

		String argument = StringArgumentType.getString(context, "value");

		try {
			Page page = new FilteredListings().getPage(argument);

			UIManager.openUIForcefully(sender, page);

		} catch (Exception e) {
			context.getSource().sendSystemMessage(Component.literal("§cSomething went wrong."));
			e.printStackTrace();
		}
		return 1;
	}
}
