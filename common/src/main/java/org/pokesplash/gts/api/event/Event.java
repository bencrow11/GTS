package org.pokesplash.gts.api.event;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Class used to control a type of event.
 * @param <T> The type of event to control.
 */
public class Event<T> {
		private ArrayList<Subscription<T>> subscriptions = new ArrayList<>(); // A list of current subscriptions to
	// the event.

	/**
	 * Method used to subscribe to the event.
	 * @param callback
	 * @return
	 */
		public Subscription<T> subscribe(Consumer<T> callback) {
			Subscription<T> sub = new Subscription<>(this, callback);
			subscriptions.add(sub);
			return sub;
		}

		// Method used to unsubscribe from the event.
		public void unsubscribe(Subscription<T> subscription) {
			subscriptions.remove(subscription);
		}

		// Method used to trigger the event.
		public void trigger(T t) {
			for (Subscription<T> sub : subscriptions) {
				sub.run(t);
			}
		}
}
