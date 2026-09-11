package org.pokesplash.gts.command.superclass;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.pokesplash.gts.util.Utils;

import java.util.function.Supplier;

/**
 * Class used to create subcommands.
 */
public abstract class Subcommand {

	// Where the usage message comes from. A supplier rather than a String so the
	// text is read when it is shown: subcommands are built once, but the language
	// file can be reloaded, and a String would keep the wording from startup.
	private final Supplier<String> usage;

	/**
	 * Constructor to create a new subcommand and generate a usage string.
	 * @param usageString
	 */
	public Subcommand(String usageString) {
		this(() -> usageString);
	}

	/**
	 * Constructor for a usage string that is read from the language file.
	 * @param usageSupplier supplies the usage text when it is needed.
	 */
	public Subcommand(Supplier<String> usageSupplier) {
		usage = usageSupplier;
	}

	/**
	 * Method to send the usage string to the player.
	 * @param context The source of the command
	 * @return integer to complete command.
	 */
	public int showUsage(CommandContext<CommandSourceStack> context) {
		context.getSource().sendSystemMessage(Component.literal(Utils.formatMessage(usage.get(), context.getSource().isPlayer())));
		return 1;
	}

	/**
	 * Method to override to create the command.
	 * @return
	 */
	public abstract CommandNode<CommandSourceStack> build();

	/**
	 * Method to override where the logic of the command goes.
	 * @param context The source of the command.
	 * @return Integer to complete the command.
	 */
	public abstract int run(CommandContext<CommandSourceStack> context);
}
