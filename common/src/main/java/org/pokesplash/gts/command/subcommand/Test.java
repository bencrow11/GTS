package org.pokesplash.gts.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.command.superclass.Subcommand;

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

		for (int x = 0; x < 100_000; x ++) {
			Gts.listings.addListing(new ItemListing(
					UUID.fromString("b5c833a0-c6f7-4e89-9ad5-d36faef37ab2"),
					"bencrow11",
					1000 + (x * 10),
					new ItemStack(Item.byId(100))
			));
		}

		return 1;
	}
}
