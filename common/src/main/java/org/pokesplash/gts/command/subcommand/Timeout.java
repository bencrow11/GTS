package org.pokesplash.gts.command.subcommand;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.command.superclass.Subcommand;
import org.pokesplash.gts.util.Utils;

import java.util.Date;

public class Timeout extends Subcommand {

	public Timeout() {
		super("§9Usage:\n§3- gts timeout <player> <time> <amount>");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("timeout")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Gts.permissions.hasPermission(ctx.getPlayer(),
								"timeout");
					} else {
						return true;
					}
				})
				.executes(this::showUsage)
				.then(Commands.argument("player", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							for (String name : Gts.server.getPlayerList().getPlayerNamesArray()) {
								builder.suggest(name);
							}
							return builder.buildFuture();
						})
						.executes(this::showUsage)
						.then(Commands.argument("time", StringArgumentType.string())
								.suggests((ctx, builder) -> {
									String[] times = new String[]{"days", "hours", "minutes", "seconds"};

									for (String time : times) {
										builder.suggest(time);
									}
									return builder.buildFuture();
								})
								.executes(this::showUsage)
								.then(Commands.argument("amount", IntegerArgumentType.integer())
										.suggests((ctx, builder) -> {
											builder.suggest(1);
											for (int x = 5; x <= 30; x++) {
												builder.suggest(x);
											}
											return builder.buildFuture();
										})
										.executes(this::run))))

				.build();
	}

	/**
	 * Method to perform the logic when the command is executed.
	 * @param context the source of the command.
	 * @return integer to complete command.
	 */
	@Override
	public int run(CommandContext<CommandSourceStack> context) {

		String playerName = StringArgumentType.getString(context, "player");

		ServerPlayer player = Gts.server.getPlayerList().getPlayerByName(playerName);

		if (player == null) {
			context.getSource().sendSystemMessage(Component.literal("§cCould not find player with name: "
					+ playerName));
			return 1;
		}

		String time = StringArgumentType.getString(context, "time");

		Integer amount = IntegerArgumentType.getInteger(context, "amount");

		long endTime;

		try {
			endTime = parseTime(time, amount);
		} catch (Exception e) {
			context.getSource().sendSystemMessage(Component.literal("§cCould not parse time "
					+ time));
			return 1;
        }

		if (!Gts.timeouts.hasTimeoutExpired(player.getUUID())) {
			Gts.timeouts.removeTimeout(player.getUUID());
			context.getSource().sendSystemMessage(Component.literal("§cRemoved timeout for " + playerName));
			return 1;
		}

		Gts.timeouts.addTimeout(player.getUUID(), endTime);

		context.getSource().sendSystemMessage(Component.literal("§b" + playerName +
				" has been timed out for " + Utils.parseLongDate(endTime - new Date().getTime())));

        return 1;
	}

	private long parseTime(String type, int amount) throws Exception {

		Date date = new Date();

		switch (type) {
			case "days":
				date.setTime(date.getTime() + (long) amount * 24 * 60 * 60 * 1000);
				break;
			case "hours":
				date.setTime(date.getTime() + (long) amount * 60 * 60 * 1000);
				break;
			case "minutes":
				date.setTime(date.getTime() + (long) amount * 60 * 1000);
				break;
			case "seconds":
				date.setTime(date.getTime() + (long) amount * 1000);
				break;
			default:
				throw new Exception("Could not parse time");
		}

		return date.getTime();
	}
}
