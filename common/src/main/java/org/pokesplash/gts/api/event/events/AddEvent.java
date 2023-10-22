package org.pokesplash.gts.api.event.events;

import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Listing.Listing;

import java.util.UUID;

public class AddEvent {
	private Listing listing;
	private UUID source;

	public AddEvent(Listing listing, ServerPlayer source) {
		this.listing = listing;
		this.source = source.getUUID();
	}

	public Listing getListing() {
		return listing;
	}

	public UUID getSource() {
		return source;
	}
}
