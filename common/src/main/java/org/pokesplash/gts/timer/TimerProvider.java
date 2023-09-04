package org.pokesplash.gts.timer;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.PokemonListing;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(listing.getSellerUuid());
					party.add(listing.getPokemon());
				} catch (NoPokemonStoreException e) {
					Gts.LOGGER.error("Could not return pokemon " + listing.getPokemon().getSpecies() + " to player: " + listing.getSellerName() +
							".\nError: " + e.getMessage());
				}
			}
		}, 1000 * 20);
		pokemonTimers.put(listing, timer);
	}

	public void addTimer(ItemListing listing) {
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Make timers remove the listing once it's up.
			}
		}, 1000 * 20);
		itemTimers.put(listing, timer);
	}
}
