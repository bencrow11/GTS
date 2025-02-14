package org.pokesplash.gts.command.superclass;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.pokesplash.gts.Gts;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class BaseCommand {

	// The command name.
	private String commandString;

	// A list of aliases for the command.
	private ArrayList<String> aliases;

	// The permission for the command.
	private String permission;

	// A list of subcommands for the base command.
	private ArrayList<Subcommand> subcommands;

	/**
	 * Constructor for super method.
	 * @param commandString The string of the base command.
	 * @param aliases A list of the aliases that should be used for this command.
	 * @param subcommands A list of the subcommands of the base command.
	 */
	public BaseCommand(String commandString, List<String> aliases, String permission,
	                   List<Subcommand> subcommands) {
		this.commandString = commandString;

		this.aliases = new ArrayList<>();
		this.aliases.addAll(aliases);

		this.permission = permission;

		this.subcommands = new ArrayList<>();
		this.subcommands.addAll(subcommands);
	}

	/**
	 * Method to register and build the command.
	 */
	public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		createCommand(dispatcher);
	}

	private void createCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralCommandNode<CommandSourceStack> root = Commands
				.literal(commandString)
				.requires(ctx -> {
					if (Gts.config.isEnablePermissionNodes()) {
						if (ctx.isPlayer()) {
							return Gts.permissions.hasPermission(ctx.getPlayer(), permission);
						} else {
							return true;
						}
					} else {
						return true;
					}
				})
				.executes(this::run)
				.build();

		dispatcher.getRoot().addChild(root);

		// Add the aliases to the base command.
		for (String alias : aliases) {
			dispatcher.register(Commands.literal(alias).redirect(root).executes(this::run));
		}

		// Adds subcommands to the base command.
		for (Subcommand subcommand : subcommands) {
			root.addChild(subcommand.build());
		}
	}

	/**
	 * Override this method to run the command.
	 * @param context the context the command is run in.
	 * @return 1 is successful or -1 is failed.
	 */
	public abstract int run(CommandContext<CommandSourceStack> context);
}
