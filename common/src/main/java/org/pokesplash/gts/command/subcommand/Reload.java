package org.pokesplash.gts.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Subcommand;
import org.pokesplash.gts.util.Utils;

public class Reload extends Subcommand {

	public Reload() {
		super("§9Usage:\n§3- gts reload");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("reload")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Gts.permissions.hasPermission(ctx.getPlayer(),
								Gts.permissions.getPermission("GtsReload"));
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

		Gts.reload();

		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(Gts.language.getReload_message(),
				context.getSource().isPlayer())));

		return 1;
	}
}
