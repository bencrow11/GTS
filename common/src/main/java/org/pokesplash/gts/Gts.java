package org.pokesplash.gts;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.entity.pokemon.PokemonServerDelegate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.world.item.Items;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.Permissions;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Gts
{
	public static final String MOD_ID = "gts";

	public static final Permissions permissions = new Permissions();

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		CommandRegistrationEvent.EVENT.register(CommandsRegistry::registerCommands);
	}
}
