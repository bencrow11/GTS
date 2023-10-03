package org.pokesplash.gts.api.event;

import java.util.function.Consumer;

/**
 * Object returned from subscribing to an event. Allows you to run the event for this subscription.
 * @param <T> The type of event that has been subscribed to.
 */
public class Subscription<T> {

	private Event<T> event;
	private Consumer<T> callback;

	public Subscription(Event<T> event, Consumer<T> callback) {
		this.event = event;
		this.callback = callback;
	}

	public void run(T t) {
		callback.accept(t);
	};
}
