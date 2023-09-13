package org.pokesplash.gts.timer;

import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;

import java.util.*;

public class TimerProvider {
	private HashMap<PokemonListing, Timer> pokemonTimers;

	private HashMap<ItemListing, Timer> itemTimers;

	public TimerProvider() {
		pokemonTimers = new HashMap<>();
		itemTimers = new HashMap<>();
	}

	public ArrayList<PokemonListing> getPokemonTimers() {
		ArrayList<PokemonListing> keys = new ArrayList<>(pokemonTimers.keySet());
		return keys;
	}

	public ArrayList<ItemListing> getItemTimers() {
		ArrayList<ItemListing> keys = new ArrayList<>(itemTimers.keySet());
		return keys;
	}

	public void deleteTimer(PokemonListing listing) {
		Timer timer = pokemonTimers.remove(listing);
		timer.cancel();
	}

	public void deleteTimer(ItemListing listing) {
		Timer timer = itemTimers.remove(listing);
		timer.cancel();
	}

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
