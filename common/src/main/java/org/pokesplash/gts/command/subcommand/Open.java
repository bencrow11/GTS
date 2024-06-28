package org.pokesplash.gts.command.subcommand;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.UI.SingleItemListing;
import org.pokesplash.gts.UI.SinglePokemonListing;
import org.pokesplash.gts.util.Subcommand;

import java.util.UUID;

public class Open extends Subcommand {

	public Open() {
		super("§9Usage:\n§3- gts <listing id>");
	}

	/**
	 * Method used to add to the base command for this subcommand.
	 *
	 * @return source to complete the command.
	 */
	@Override
	public CommandNode<CommandSourceStack> build() {
		return Commands.argument("id", UuidArgument.uuid())
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
			context.getSource().sendSystemMessage(Component.literal("This command must be ran by a player."));
			return 1;
		}

		ServerPlayer sender = context.getSource().getPlayer();

		UUID id = UuidArgument.getUuid(context, "id");

		Listing listing = Gts.listings.getActiveListingById(id);
		if (listing != null) {
			if (listing instanceof PokemonListing) {
				UIManager.openUIForcefully(sender, new SinglePokemonListing().getPage(sender, (PokemonListing) listing));
			} else {
				UIManager.openUIForcefully(sender, new SingleItemListing().getPage(sender, (ItemListing) listing));
			}
		}
		return 1;
	}
}
