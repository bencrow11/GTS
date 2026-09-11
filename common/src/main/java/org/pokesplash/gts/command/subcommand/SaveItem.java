package org.pokesplash.gts.command.subcommand;

import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.serialization.JsonOps;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.command.superclass.Subcommand;
import org.pokesplash.gts.util.Utils;

import java.util.concurrent.CompletableFuture;

public class SaveItem extends Subcommand {

	public SaveItem() {
		super(() -> Gts.language.getUsageSaveItem());
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 * @return source to complete the command.
	 */
	@Override
	public LiteralCommandNode<CommandSourceStack> build() {
		return Commands.literal("saveitem")
				.requires(ctx -> {
					if (ctx.isPlayer()) {
						return Gts.permissions.hasPermission(ctx.getPlayer(),
								"saveitem");
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
			context.getSource().sendSystemMessage(Component.literal(
					Gts.language.getPlayerOnlyCommand()
			));
			return 1;
		}

		try {
			ItemStack item = context.getSource().getPlayer().getMainHandItem();

			// If they aren't holding an item. Message them
			if (item.getItem().equals(Items.AIR)) {
				context.getSource().sendSystemMessage(Component.literal(Gts.language.getNotHoldingItem()));
				return 1;
			}

			JsonElement jsonElement = ItemStack.CODEC.encodeStart(Gts.server.registryAccess().createSerializationContext(JsonOps.INSTANCE),
					item).getOrThrow();
			String data = Utils.newGson().toJson(jsonElement);
			CompletableFuture<Boolean> result = Utils.writeFileAsync("/config/gts/", "item.json", data);

			if (result.join()) {
				context.getSource().sendSystemMessage(Component.literal(Gts.language.getSaveItemSuccess()));
			} else {
				context.getSource().sendSystemMessage(Component.literal(Gts.language.getSaveItemFail()));
			}
		} catch (Exception e) {
			context.getSource().sendSystemMessage(Component.literal(e.getMessage()));
			e.printStackTrace();
		}

		return 1;
	}
}
