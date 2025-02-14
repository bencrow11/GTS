package org.pokesplash.gts.command.subcommand;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.command.superclass.Subcommand;

public class GetPrice extends Subcommand {

	public GetPrice() {
		super("§9Usage:\n§3- gts getprice [slot]");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("getprice")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Gts.permissions.hasPermission(ctx.getPlayer(),
								"getprice");
					} else {
						return true;
					}
				})
				.executes(this::runItem)
					.then(Commands.argument("slot", IntegerArgumentType.integer(1, 6))
					.executes(this::runPokemon))
				.build();
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) {
		return 0;
	}

	public int runItem(CommandContext<CommandSourceStack> context) {
		if (!context.getSource().isPlayer()) {
			context.getSource().sendSystemMessage(Component.literal(
					"This command must be ran by a player."
			));
			return 1;
		}

		try {
			ServerPlayer sender = context.getSource().getPlayer();

			ItemStack item = sender.getMainHandItem();

			if (item.isEmpty()) {
				context.getSource().sendSystemMessage(Component.literal(
						"§cYou are not holding an item."
				));
				return 1;
			}

			double price = Gts.history.getAveragePrice(item);

			if (price != 0) {
				context.getSource().sendSystemMessage(Component.literal(
						"§aAverage price is $" + price + "."
				));
			} else {
				context.getSource().sendSystemMessage(Component.literal(
						"§cThis item has not been sold before."
				));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * Method to perform the logic when the command is executed.
	 * @param context the source of the command.
	 * @return integer to complete command.
	 */
	public int runPokemon(CommandContext<CommandSourceStack> context) {
		if (!context.getSource().isPlayer()) {
			context.getSource().sendSystemMessage(Component.literal(
					"This command must be ran by a player."
			));
			return 1;
		}

		ServerPlayer sender = context.getSource().getPlayer();

		int slot = IntegerArgumentType.getInteger(context, "slot");

		Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty(sender).get(slot - 1);

		if (pokemon == null) {
			context.getSource().sendSystemMessage(Component.literal(
					"§cNo Pokemon found in slot " + slot + "."
			));
			return 1;
		}

		double price = Gts.history.getAveragePrice(pokemon);

		if (price != 0) {
			context.getSource().sendSystemMessage(Component.literal(
					"§aAverage price is $" + price + "."
			));
			return 1;
		} else {
			context.getSource().sendSystemMessage(Component.literal(
					"§cThis Pokemon has not been sold before."
			));
			return 1;
		}
	}
}
