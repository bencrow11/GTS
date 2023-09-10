package org.pokesplash.gts.timer;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
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

	public Timer getTimer(PokemonListing listing) {
		return pokemonTimers.get(listing);
	}

	public Timer getTimer(ItemListing listing) {
		return itemTimers.get(listing);
	}

	public void deleteTimer(PokemonListing listing) {
		pokemonTimers.remove(listing);
	}

	public void deleteTimer(ItemListing listing) {
		itemTimers.remove(listing);
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
