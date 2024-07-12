package org.pokesplash.gts.moderation;

import com.google.gson.Gson;
import org.pokesplash.gts.util.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Used to track player timouts.
 */
public class TimeoutProvider {
    private final String FILEPATH = "/config/gts";
    private final String FILENAME = "timeout.json";
    private HashMap<String, Long> timeouts;

    /**
     * Constructor to initialise the timeouts.
     */
    public TimeoutProvider() {
        timeouts = new HashMap<>();
    }

    /**
     * Has the player finished their timeout.
     * @param player The player to check.
     * @return True if the end time is before the current date.
     */
    public boolean hasTimeoutExpired(UUID player) {
        if (containsPlayer(player)) {
            long endTime = timeouts.get(player.toString());
            if (endTime < new Date().getTime()) {
                removeTimeout(player);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the players timeout.
     * @param player The player to fetch.
     * @return The end time of their timeout.
     */
    public Long getTimeout(UUID player) {
        return timeouts.get(player.toString());
    }

    /**
     * Is the player in the hashmap.
     * @param player The player to check for.
     * @return True if the player is in the hashmap.
     */
    private boolean containsPlayer(UUID player) {
        return timeouts.containsKey(player.toString());
    }

    /**
     * Adds a new player and time to the hashmap.
     * @param player The players UUID.
     * @param endTime The time the timeout ends.
     */
    public void addTimeout(UUID player, long endTime) {
        if (containsPlayer(player)) {
            removeTimeout(player);
        }
        timeouts.put(player.toString(), endTime);
        write();
    }

    /**
     * Removes a player from the timeout.
     * @param player The player to remove.
     */
    public void removeTimeout(UUID player) {
        timeouts.remove(player.toString());
        write();
    }

    /**
     * Writes to file.
     */
    private void write() {
        Utils.writeFileAsync(FILEPATH, FILENAME,
                Utils.newGson().toJson(timeouts));
    }

    /**
     * Reads from file.
     */
    public void read() {
        Utils.readFileAsync(FILEPATH, FILENAME, e -> {
            Gson gson = Utils.newGson();
            HashMap<String, Long> data = gson.fromJson(e, HashMap.class);
            timeouts = data;
        });
    }
}
