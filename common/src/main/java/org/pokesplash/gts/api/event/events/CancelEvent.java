package org.pokesplash.gts.api.event.events;

import org.pokesplash.gts.Listing.Listing;

public class CancelEvent {
	private Listing listing;

	public CancelEvent(Listing listing) {
		this.listing = listing;
	}

	public Listing getListing() {
		return listing;
	}
}
