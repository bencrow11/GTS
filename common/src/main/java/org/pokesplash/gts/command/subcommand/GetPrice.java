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
		super(() -> Gts.language.getUsageGetPrice());
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
					Gts.language.getPlayerOnlyCommand()
			));
			return 1;
		}

		try {
			ServerPlayer sender = context.getSource().getPlayer();

			ItemStack item = sender.getMainHandItem();

			if (item.isEmpty()) {
				context.getSource().sendSystemMessage(Component.literal(
						Gts.language.getNotHoldingItem()
				));
				return 1;
			}

			double price = Gts.history.getAveragePrice(item);

			if (price != 0) {
				context.getSource().sendSystemMessage(Component.literal(
						Gts.language.getAveragePrice().replace("{price}", String.valueOf(price))
				));
			} else {
				context.getSource().sendSystemMessage(Component.literal(
						Gts.language.getItemNeverSold()
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
					Gts.language.getPlayerOnlyCommand()
			));
			return 1;
		}

		ServerPlayer sender = context.getSource().getPlayer();

		int slot = IntegerArgumentType.getInteger(context, "slot");

		Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty(sender).get(slot - 1);

		if (pokemon == null) {
			context.getSource().sendSystemMessage(Component.literal(
					Gts.language.getNoPokemonFoundInSlot().replace("{slot}", String.valueOf(slot))
			));
			return 1;
		}

		double price = Gts.history.getAveragePrice(pokemon);

		if (price != 0) {
			context.getSource().sendSystemMessage(Component.literal(
					Gts.language.getAveragePrice().replace("{price}", String.valueOf(price))
			));
			return 1;
		} else {
			context.getSource().sendSystemMessage(Component.literal(
					Gts.language.getPokemonNeverSold()
			));
			return 1;
		}
	}
}
