package org.pokesplash.gts.api.event;

import org.pokesplash.gts.api.event.events.PurchaseEvent;

/**
 * Class that holds all of the events.
 */
public abstract class GtsEvents {
	public static Event<PurchaseEvent> PURCHASE_EVENT = new Event<>();
}
