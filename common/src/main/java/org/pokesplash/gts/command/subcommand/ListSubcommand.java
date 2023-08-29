package org.pokesplash.gts.command.subcommand;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.pokesplash.gts.util.Subcommand;
import org.pokesplash.gts.util.Utils;

public class ListSubcommand extends Subcommand {

	public ListSubcommand() {
		super("This is the subcommand usage.");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("sell")
				.executes(this::showUsage)
				.then(Commands.argument("player", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							for (String name : ctx.getSource().getOnlinePlayerNames()) {
								builder.suggest(name);
							}
							return builder.buildFuture();
						})
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

		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage("subcommand ran!",
				context.getSource().isPlayer())));

		return 1;
	}
}
