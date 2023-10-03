package org.pokesplash.gts.api.event.events;

import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Listing.Listing;

import java.util.UUID;

/**
 * Event triggered when a purchase is successful.
 */
public class PurchaseEvent {
	private ServerPlayer buyer;
	private Listing listing;

	public PurchaseEvent(ServerPlayer buyer, Listing listing) {

		this.buyer = buyer;
		this.listing = listing;
	}

	public ServerPlayer getBuyer() {
		return buyer;
	}

	public Listing getProduct() {
		return listing;
	}
}
