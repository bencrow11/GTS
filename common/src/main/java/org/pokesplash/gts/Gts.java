package org.pokesplash.gts;

import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.api.provider.HistoryProviderAPI;
import org.pokesplash.gts.api.provider.ListingsProviderAPI;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.config.Config;
import org.pokesplash.gts.config.Lang;
import org.pokesplash.gts.history.HistoryProvider;
import org.pokesplash.gts.moderation.TimeoutProvider;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.GtsLogger;
import org.pokesplash.gts.util.Permissions;

import java.util.Date;
import java.util.UUID;

public class Gts
{
	public static final String MOD_ID = "gts";
	public static final String LISTING_FILE_PATH = "/config/gts/listings";
	public static final String CONFIG_FILE_VERSION = "2.1";
	public static final String LANG_FILE_VERSION = "2.4";
	public static final String LISTING_FILE_VERSION = "2.0";
	public static final String HISTORY_FILE_VERSION = "2.0";
	public static boolean isDebugMode = false;
	public static int debugTime = 10;
	public static int ticksPerCheck = 20;
	public static Config config;
	public static final Permissions permissions = new Permissions();
	public static ListingsProvider listings;
	public static HistoryProvider history;
	public static TimeoutProvider timeouts;
	public static final GtsLogger LOGGER = new GtsLogger();
	public static Lang language;
	public static MinecraftServer server;

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		reload();
	}


	public static void reload() {
		config = new Config();
		listings = ListingsProviderAPI.getHighestPriority();
		history = HistoryProviderAPI.getHighestPriority();
		language = new Lang();
		timeouts = new TimeoutProvider();
		config.init();
		listings.init();
		history.init();
		language.init();
		timeouts.read();
	}
}
