package org.pokesplash.gts;

import net.minecraft.server.MinecraftServer;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.api.provider.HistoryProviderAPI;
import org.pokesplash.gts.api.provider.ListingsProviderAPI;
import org.pokesplash.gts.command.basecommand.GtsCommand;
import org.pokesplash.gts.config.Config;
import org.pokesplash.gts.config.ConfigManager;
import org.pokesplash.gts.config.Lang;
import org.pokesplash.gts.config.LangManager;
import org.pokesplash.gts.history.HistoryProvider;
import org.pokesplash.gts.moderation.TimeoutProvider;
import org.pokesplash.gts.permission.PermissionProvider;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.GtsLogger;

public class Gts
{
	public static final String MOD_ID = "gts";
	public static final String LISTING_FILE_PATH = "/config/gts/listings";
	public static final int CONFIG_FILE_VERSION = 3;
	public static final int LANG_FILE_VERSION = 3;
	public static final String LISTING_FILE_VERSION = "2.0";
	public static final String HISTORY_FILE_VERSION = "2.0";
	public static boolean isDebugMode = false;
	public static int debugTime = 10;
	public static int ticksPerCheck = 20;
	public static Config config;
	public static PermissionProvider permissions;
	public static ListingsProvider listings;
	public static HistoryProvider history;
	public static TimeoutProvider timeouts;
	public static final GtsLogger LOGGER = new GtsLogger();
	public static Lang language;
	public static MinecraftServer server;
    public static boolean showPokemonDisplayName = true;

	public static void init() {
		CommandsRegistry.addCommand(new GtsCommand());
		reloadNonSensitive();
	}


	public static void reload() {
		reloadNonSensitive();
		reloadSensitive();
	}

	public static void reloadNonSensitive() {
		listings = ListingsProviderAPI.getHighestPriority();
		history = HistoryProviderAPI.getHighestPriority();
		timeouts = new TimeoutProvider();
		listings.init();
		history.init();
		timeouts.read();
	}

	public static void reloadSensitive() {
		config = new ConfigManager().loadConfig();
		language = new LangManager().loadLangFile();
	}
 }
