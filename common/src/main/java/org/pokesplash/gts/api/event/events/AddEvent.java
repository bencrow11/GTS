package org.pokesplash.gts.api.event.events;

import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Listing.Listing;

public class AddEvent {
	private Listing listing;
	private ServerPlayer source;

	public AddEvent(Listing listing, ServerPlayer source) {
		this.listing = listing;
		this.source = source;
	}

	public Listing getListing() {
		return listing;
	}

	public ServerPlayer getSource() {
		return source;
	}
}
