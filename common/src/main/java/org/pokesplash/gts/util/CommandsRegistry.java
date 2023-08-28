package org.pokesplash.gts.util;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;

/**
 * Class to fetch all commands.
 */
public abstract class CommandsRegistry {
	// Arraylist that holds all commands.
	private static ArrayList<BaseCommand> commands = new ArrayList<>();

	/**
	 * Method to add a new command.
	 * @param command The command to add.
	 */
	public static void addCommand(BaseCommand command) {
		commands.add(command);
	}

	/**
	 * Registers commands methods for fabric and forge.
	 * @param dispatcher the dispatcher used to register the commands.
	 */
	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		for (BaseCommand command : commands) {
			command.register(dispatcher);
		}
	}
	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
		registerCommands(dispatcher);
	}
}
