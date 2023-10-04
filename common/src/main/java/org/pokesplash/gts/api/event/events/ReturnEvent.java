package org.pokesplash.gts.api.event.events;

import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Listing.Listing;

public class ReturnEvent {
	private ServerPlayer player;
	private Listing listing;

	public ReturnEvent(ServerPlayer player, Listing listing) {
		this.player = player;
		this.listing = listing;
	}

	public ServerPlayer getPlayer() {
		return player;
	}

	public Listing getListing() {
		return listing;
	}
}
