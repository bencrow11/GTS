package org.pokesplash.gts.api.event;

import org.pokesplash.gts.api.event.events.AddEvent;
import org.pokesplash.gts.api.event.events.CancelEvent;
import org.pokesplash.gts.api.event.events.PurchaseEvent;
import org.pokesplash.gts.api.event.events.ReturnEvent;

/**
 * Class that holds all of the events.
 */
public abstract class GtsEvents {
	public static Event<PurchaseEvent> PURCHASE = new Event<>();
	public static Event<CancelEvent> CANCEL = new Event<>();
	public static Event<AddEvent> ADD = new Event<>();
	public static Event<ReturnEvent> RETURN = new Event<>();
}
