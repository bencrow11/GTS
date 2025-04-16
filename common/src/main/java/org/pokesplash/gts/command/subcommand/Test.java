package org.pokesplash.gts.command.subcommand;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.pokesplash.gts.command.superclass.Subcommand;

import java.util.ArrayList;
import java.util.List;

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

		ItemStack item = context.getSource().getPlayer().getMainHandItem();

		CompoundTag tag = new CompoundTag();
		tag.putIntArray("ivs", new ArrayList<>(List.of(31, 1, 3, 31, 31, 6)));
		item.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

		return 1;
	}
}
