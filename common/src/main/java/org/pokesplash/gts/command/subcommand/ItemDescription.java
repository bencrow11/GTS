package org.pokesplash.gts.command.subcommand;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.command.superclass.Subcommand;
import org.pokesplash.gts.util.Utils;

public class ItemDescription extends Subcommand {

	public ItemDescription() {
		super(() -> Gts.language.getUsageItemDesc());
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("itemdesc")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Gts.permissions.hasPermission(ctx.getPlayer(),
								"itemdesc");
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
            context.getSource().sendSystemMessage(Component.literal(Gts.language.getPlayerOnlyCommand()));
            return 1;
        }

		ItemStack handItem = context.getSource().getPlayer().getMainHandItem();

        if (handItem.is(Items.AIR)) {
            context.getSource().sendSystemMessage(Component.literal(
                    Gts.language.getNotHoldingItem()
            ));
            return 1;
        }

        context.getSource().sendSystemMessage(Component.literal(
                handItem.getItem().getDescriptionId()
        ));

		return 1;
	}
}
