package org.pokesplash.gts.timer;

import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.Listing.PokemonListing;

import java.util.*;

public class TimerProvider {
	@Deprecated
	private HashMap<PokemonListing, Timer> pokemonTimers;

	@Deprecated
	private HashMap<ItemListing, Timer> itemTimers;

	private HashMap<Listing, Timer> timers;

	public TimerProvider() {
		pokemonTimers = new HashMap<>(); // TODO Remove
		itemTimers = new HashMap<>(); // TODO Remove

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
		timer.cancel();
	}

	/**
	 * Adds a timer for a given listing.
	 * @param listing The listing to add a timer for.
	 */
	public void addTimer(Listing listing) {
		long timeDiff = listing.getEndTime() - new Date().getTime();

		if (timeDiff > 0) {
			Timer timer = new Timer();

			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Gts.listings.removeListing(listing);
					Gts.listings.addExpiredListing(listing);
				}
			}, timeDiff);
			timers.put(listing, timer);
		} else {
			Gts.listings.removeListing(listing);
			Gts.listings.addExpiredListing(listing);
		}
	}

	@Deprecated
	public ArrayList<PokemonListing> getPokemonTimers() {
		ArrayList<PokemonListing> keys = new ArrayList<>(pokemonTimers.keySet());
		return keys;
	}

	@Deprecated
	public ArrayList<ItemListing> getItemTimers() {
		ArrayList<ItemListing> keys = new ArrayList<>(itemTimers.keySet());
		return keys;
	}

	@Deprecated
	public void deleteTimer(PokemonListing listing) {
		Timer timer = pokemonTimers.remove(listing);
		timer.cancel();
	}

	@Deprecated
	public void deleteTimer(ItemListing listing) {
		Timer timer = itemTimers.remove(listing);
		timer.cancel();
	}

	@Deprecated
	public void addTimer(PokemonListing listing) {
		long timeDiff = listing.getEndTime() - new Date().getTime();

		if (timeDiff > 0) {
			Timer timer = new Timer();

			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Gts.listings.removePokemonListing(listing);
					Gts.listings.addExpiredPokemonListing(listing);
				}
			}, timeDiff);
			pokemonTimers.put(listing, timer);
		} else {
			Gts.listings.removePokemonListing(listing);
			Gts.listings.addExpiredPokemonListing(listing);
		}
	}

	@Deprecated
	public void addTimer(ItemListing listing) {

		long timeDiff = listing.getEndTime() - new Date().getTime();

		if (timeDiff > 0) {
			Timer timer = new Timer();

			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Gts.listings.removeItemListing(listing);
					Gts.listings.addExpiredItemListing(listing);
				}
			}, timeDiff);
			itemTimers.put(listing, timer);
		} else {
			Gts.listings.removeItemListing(listing);
			Gts.listings.addExpiredItemListing(listing);
		}
	}
}
