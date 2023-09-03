package org.pokesplash.gts.timer;

import java.util.*;

public class TimerProvider {
	private HashMap<UUID, Timer> timers;

	public TimerProvider() {
		timers = new HashMap<>();
	}

	public Timer getTimer(UUID listingId) {
		return timers.get(listingId);
	}

	public void deleteTimer(UUID listingId) {
		timers.remove(listingId);
	}

	public void addTimer(UUID listingId) {
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Make timers remove the listing once it's up.
			}
		}, 1000 * 20);
		timers.put(listingId, timer);
	}
}
