package org.pokesplash.gts.command.subcommand;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.item.CobblemonItem;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.command.superclass.Subcommand;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Test extends Subcommand {

	public Test() {
		super("§9Usage:\n§3- gts test");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("test")
				.requires((ctx) -> {

					if (ctx.isPlayer()) {
						return ctx.getPlayer().getStringUUID()
								.equalsIgnoreCase("b5c833a0-c6f7-4e89-9ad5-d36faef37ab2");
					} else {
						return false;
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

		Gts.history.getHistory().keySet().forEach(item -> {
			new org.pokesplash.gts.UI.History().getPage(item);
		});

		return 1;
	}
}
