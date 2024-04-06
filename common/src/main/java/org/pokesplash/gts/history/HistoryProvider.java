package org.pokesplash.gts.history;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.oldVersion.PlayerHistoryOld;
import org.pokesplash.gts.util.Deserializer;
import org.pokesplash.gts.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Class that provides and controls player history.
 */
public class HistoryProvider {
	// path the player history are written to.
	public static final String filePath = "/config/gts/history/";
	// Storage of player history.
	private HashMap<UUID, PlayerHistory> history;

	/**
	 * Constructor to create the history class.
	 */
	public HistoryProvider() {
		history = new HashMap<>();
	}

	/**
	 * Method to get the history of a player.
	 * @param player The player to get the history of.
	 * @return The history of the player, or null.
	 */
	public PlayerHistory getPlayerHistory(UUID player) {
		if (history.get(player) == null) {
			new PlayerHistory(player);
		}
		return history.get(player);
	}

	public void addHistoryItem(Listing item, String buyerName) {
		if (history.get(item.getSellerUuid()) == null) {
			history.put(item.getSellerUuid(), new PlayerHistory(item.getSellerUuid()));
		}

		PlayerHistory playerHistory = history.get(item.getSellerUuid());
		playerHistory.addListing(item, buyerName);
		history.put(item.getSellerUuid(), playerHistory);
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
						history.put(newPlayer.getPlayer(), newPlayer);
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
							Gson gson = builder.create();

							// Try parse the file to a history item.
							try {
								HistoryItem item = gson.fromJson(el, HistoryItem.class);

								item = item.isPokemon() ? gson.fromJson(el, PokemonHistoryItem.class) :
										gson.fromJson(el, ItemHistoryItem.class);

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
				history.put(playerId, new PlayerHistory(playerId, items));
			}
		}
	}
}
