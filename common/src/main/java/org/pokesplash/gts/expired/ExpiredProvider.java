package org.pokesplash.gts.expired;

import com.google.gson.Gson;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Class that provides and controls player history.
 */
public class ExpiredProvider {
	// path the player history are written to.
	private String filePath = "/config/gts/history/";
	// Storage of player history.
	private HashMap<UUID, PlayerExpired> history;

	/**
	 * Constructor to create the history class.
	 */
	public ExpiredProvider() {
		history = new HashMap<>();
	}

	/**
	 * Method to get the history of a player.
	 * @param player The player to get the history of.
	 * @return The history of the player, or null.
	 */
	public PlayerExpired getPlayerHistory(UUID player) {
		return history.get(player);
	}

	/**
	 * Method to update player history. This method doesn't need to be used as
	 * editing the PlayerHistory object automatically calls this method.
	 * @param playerExpired The new PlayerHistory object.
	 */
	public void updatePlayerHistory(PlayerExpired playerExpired) {
		history.put(playerExpired.getPlayer(), playerExpired);
		if (!write(playerExpired)) {
			Gts.LOGGER.error("Player History for player " + playerExpired.getPlayer() + " could not be written to " +
					"file.");
		}
	}

	/**
	 * Method to write a player history to file.
	 * @param playerExpired The PlayerHistory object to write.
	 * @return true if the write was successful.
	 */
	private boolean write(PlayerExpired playerExpired) {
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
		File dir = Utils.checkForDirectory("/config/gts/history/");

		String[] list = dir.list();

		if (list.length == 0) {
			return;
		}

		for (String file : list) {
			Utils.readFileAsync(filePath, file, el -> {
				Gson gson = Utils.newGson();
				PlayerExpired player = gson.fromJson(el, PlayerExpired.class);
				history.put(player.getPlayer(), player);
			});
		}
	}
}
