package org.pokesplash.gts.timer;

import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.Listing;

import java.util.*;

public class TimerProvider {
	private HashMap<Listing, Timer> timers;

	public TimerProvider() {
		timers = new HashMap<>();
	}

	/**
	 * Gets all Keys to the timers.
	 * @return List of keys for all timers.
	 */
	public ArrayList<Listing> getTimerKeys() {
        return new ArrayList<>(timers.keySet());
	}

	/**
	 * Deletes a timer from a given listing.
	 * @param listing The listing to delete the timer for.
	 */
	public void deleteTimer(Listing listing) {
		Timer timer = timers.remove(listing);
		if (timer != null) {
			timer.cancel();
		}
	}

	/**
	 * Adds a timer for a given listing.
	 * @param listing The listing to add a timer for.
	 */
	public void addTimer(Listing listing) {

		// If end time is -1, don't set a timer.
		if (listing.getEndTime() == -1) {
			return;
		}

		long timeDiff = listing.getEndTime() - new Date().getTime();

		if (timeDiff > 0) {
			Timer timer = new Timer();

			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (Gts.listings.removeListing(listing)) {
						Gts.listings.addExpiredListing(listing);
					}
				}
			}, timeDiff);
			timers.put(listing, timer);
		} else {
			if (Gts.listings.removeListing(listing)) {
				Gts.listings.addExpiredListing(listing);
			}
		}
	}

	public void deleteAllTimers() {
		for (Timer timer : timers.values()) {
			timer.cancel();
		}
		timers.clear();
	}

}
