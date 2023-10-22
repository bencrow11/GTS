package org.pokesplash.gts.api.event.events;

import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Listing.Listing;

import java.util.UUID;

public class ReturnEvent {
	private UUID player;
	private Listing listing;

	public ReturnEvent(ServerPlayer player, Listing listing) {
		this.player = player.getUUID();
		this.listing = listing;
	}

	public UUID getPlayer() {
		return player;
	}

	public Listing getListing() {
		return listing;
	}
}
