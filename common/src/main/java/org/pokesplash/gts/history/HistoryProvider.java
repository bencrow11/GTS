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
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Class that provides and controls player history.
 */
public class HistoryProvider {
	// path the player history are written to.
	private String filePath = "/config/gts/history/";
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

	/**
	 * Method to update player history. This method doesn't need to be used as
	 * editing the PlayerHistory object automatically calls this method.
	 * @param playerHistory The new PlayerHistory object.
	 */
	public void updatePlayerHistory(PlayerHistory playerHistory) {
		history.put(playerHistory.getPlayer(), playerHistory);
		if (!write(playerHistory)) {
			Gts.LOGGER.error("Player History for player " + playerHistory.getPlayer() + " could not be written to " +
					"file.");
		}
	}

	/**
	 * Method to write a player history to file.
	 * @param playerExpired The PlayerHistory object to write.
	 * @return true if the write was successful.
	 */
	private boolean write(PlayerHistory playerExpired) {
		Gson gson = Utils.newGson();

		CompletableFuture<Boolean> future = Utils.writeFileAsync(filePath, playerExpired.getPlayer() +
						".json",
				gson.toJson(playerExpired));

		return future.join();
	}

	/**
	 * Method to initialize the HistoryProvider object with all PlayerHistory in file.
	 */
	public void init() {
		File dir = Utils.checkForDirectory(filePath);

		String[] list = dir.list();

		if (list.length == 0) {
			return;
		}

		for (String file : list) {
			Utils.readFileAsync(filePath, file, el -> {
				GsonBuilder builder = new GsonBuilder();
				// Type adapters help gson deserialize the listings interface.
				builder.registerTypeAdapter(HistoryItem.class, new Deserializer(PokemonHistoryItem.class));
				builder.registerTypeAdapter(HistoryItem.class, new Deserializer(ItemHistoryItem.class));
				Gson gson = builder.create();

				PlayerHistory player = gson.fromJson(el, PlayerHistory.class);

				// If the file version doesn't exist or isn't correct, update it.
				if (player.version() == null || !player.version().equals(Gts.HISTORY_FILE_VERSION)) {
					PlayerHistoryOld oldPlayer = gson.fromJson(el, PlayerHistoryOld.class); // Load from old class.
					PlayerHistory newPlayer = new PlayerHistory(oldPlayer);
					Utils.deleteFile(filePath, file); // Delete the old file.
					Utils.writeFileAsync(filePath, file, gson.toJson(newPlayer)); // Write the new file.
					history.put(oldPlayer.getPlayer(), newPlayer);
				} else {
					// Otherwise just add to history.
					history.put(player.getPlayer(), player);
				}
			});
		}
	}
}
