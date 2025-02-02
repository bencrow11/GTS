package org.pokesplash.gts.history;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.api.provider.HistoryAPI;
import org.pokesplash.gts.oldVersion.PlayerHistoryOld;
import org.pokesplash.gts.util.Deserializer;
import org.pokesplash.gts.util.Utils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class that provides and controls player history.
 */
public class HistoryProvider {
	// path the player history are written to.
	public static final String filePath = "/config/gts/history/";
	private static final String brokeFilesPath = "/config/gts/invalid/history/";
	// Storage of player history.
	protected HashMap<UUID, PlayerHistory> history;

	/**
	 * Constructor to create the history class.
	 */
	public HistoryProvider() {
		history = new HashMap<>();
	}

	public HashMap<UUID, PlayerHistory> getHistory() {
		return history;
	}

	protected void putHistory(UUID uuid, PlayerHistory playerHistory) {
		history.put(uuid, playerHistory);
	}

	protected void removeHistory(UUID uuid) {
		history.remove(uuid);
	}

	/**
	 * Method to get the history of a player.
	 * @param player The player to get the history of.
	 * @return The history of the player, or null.
	 */
	public PlayerHistory getPlayerHistory(UUID player) {
		if (getHistory().get(player) == null) {
			if (HistoryAPI.getHighestPriority() == null) {
				new PlayerHistory(player);
			} else {
				putHistory(player, new PlayerHistory(player));
			}

		}
		return getHistory().get(player);
	}

	public HistoryItem findHistoryById(UUID id) {
		for (PlayerHistory h : getHistory().values()) {
			for (HistoryItem item : h.getListings()) {
				if (item.getId().equals(id)) {
					return item;
				}
			}
		}

		return null;
	}

	public void updateHistory(PlayerHistory history) {
		putHistory(history.getPlayer(), history);
	}

	public void addHistoryItem(Listing item, String buyerName) {
		if (getHistory().get(item.getSellerUuid()) == null) {
			putHistory(item.getSellerUuid(), new PlayerHistory(item.getSellerUuid()));
		}

		PlayerHistory playerHistory = getHistory().get(item.getSellerUuid());
		playerHistory.addListing(item, buyerName);
		putHistory(item.getSellerUuid(), playerHistory);
	}

	public double getAveragePrice(ItemStack itemStack) {

		double total = 0;
		int amount = 0;

		for (PlayerHistory h : getHistory().values()) {
			for (ItemHistoryItem item : h.getItemListings()) {
				if (ItemStack.isSameItem(item.getListing(), itemStack)) {
					total += item.getPrice();
					amount ++;
				}
			}
		}

		if (total == 0) {
			return 0;
		}

		BigDecimal bd = BigDecimal.valueOf(total / amount);
		return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public double getAveragePrice(Pokemon pokemon) {
		double total = 0;
		int amount = 0;

		for (PlayerHistory h : getHistory().values()) {
			for (PokemonHistoryItem mon : h.getPokemonListings()) {
				if (mon.getListing().getSpecies().getNationalPokedexNumber() ==
				pokemon.getSpecies().getNationalPokedexNumber()) {
					total += mon.getPrice();
					amount ++;
				}
			}
		}

		if (total == 0) {
			return 0;
		}

		BigDecimal bd = BigDecimal.valueOf(total / amount);
		return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * Method to initialize the HistoryProvider object with all PlayerHistory in file.
	 */
	public void init() {
		File dir = Utils.checkForDirectory(filePath);

		File[] files = dir.listFiles();

		for (File file : files) {

			// If it is a file, see if it's an old history file and convert it.
			// TODO Remove this in a later update.
			if (file.isFile()) {
				Utils.readFileAsync(filePath, file.getName(), el -> {
					Gson gson = new Gson();
					try {
						PlayerHistoryOld oldPlayer = gson.fromJson(el, PlayerHistoryOld.class); // Load from old class.
						PlayerHistory newPlayer = new PlayerHistory(oldPlayer);
						Utils.deleteFile(filePath, file.getName()); // Delete the old file.
						putHistory(newPlayer.getPlayer(), newPlayer);
					} catch (Exception e) {
						Gts.LOGGER.error("Could not convert file " + file.getName() + " to a GTS Player History");
						e.printStackTrace();
					}
				});
			}
			// Otherwise, read the directory and store the files in memory.
			else {
				File[] playerFiles = file.listFiles();
				// A list of the players history.
				ArrayList<HistoryItem> items = new ArrayList<>();
				UUID playerId = UUID.fromString(file.getName());

				// For each file in the players directory
				for (File playerFile : playerFiles) {
					// If it is a file, try read it.
					if (playerFile.isFile()) {
						Utils.readFileAsync(filePath + file.getName() + "/",
								playerFile.getName(), el -> {
							GsonBuilder builder = new GsonBuilder();
							// Type adapters help gson deserialize the listings interface.
							builder.registerTypeAdapter(HistoryItem.class, new Deserializer(PokemonHistoryItem.class));
							builder.registerTypeAdapter(HistoryItem.class, new Deserializer(ItemHistoryItem.class));
							Gson gson = builder.setPrettyPrinting().create();

							// Try parse the file to a history item.
							try {
								HistoryItem item = gson.fromJson(el, HistoryItem.class);

								item = item.isPokemon() ? gson.fromJson(el, PokemonHistoryItem.class) :
										gson.fromJson(el, ItemHistoryItem.class);

								if (!item.isHistoryItemValid()) {
									System.out.println("[GTS] Invalid file: " + playerFile.getName() + " has been removed.");
									Utils.writeFileAsync(brokeFilesPath, playerFile.getName(),
											gson.toJson(item));
									Utils.deleteFile(filePath + file.getName() + "/", playerFile.getName());
									return;
								}

								// If the file version isn't the same as the version needed, update it.
								if (!item.getVersion().equals(Gts.HISTORY_FILE_VERSION)) {
									// TODO update file (Future)
								}

								items.add(item);

							} catch (Exception e) {
								Gts.LOGGER.error("Could not read player GTS History file for " + file.getName());
								e.printStackTrace();
							}
						});
					}
				}

				// Adds the player history to memory.
				putHistory(playerId, new PlayerHistory(playerId, items));
			}
		}
	}
}
