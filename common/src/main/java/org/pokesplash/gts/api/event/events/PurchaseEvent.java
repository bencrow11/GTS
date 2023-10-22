package org.pokesplash.gts.api.event.events;

import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Listing.Listing;

import java.util.UUID;

/**
 * Event triggered when a purchase is successful.
 */
public class PurchaseEvent {
	private UUID buyer;
	private Listing listing;

	public PurchaseEvent(ServerPlayer buyer, Listing listing) {
		this.buyer = buyer.getUUID();
		this.listing = listing;
	}

	public UUID getBuyer() {
		return buyer;
	}

	public Listing getProduct() {
		return listing;
	}
}
