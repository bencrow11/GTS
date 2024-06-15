package org.pokesplash.gts.api.provider;

import org.pokesplash.gts.Listing.ListingsProvider;

import java.util.HashMap;
import java.util.Map;

public abstract class ListingAPI {
    private static Map<Priority, ListingInterface> providers = new HashMap<>();

    public static void add(Priority priority, ListingInterface provider) {
        providers.put(priority, provider);
    }

    public static void remove(Priority priority) {
        providers.remove(priority);
    }

    public static Map<Priority, ListingInterface> getProviders() {
        return providers;
    }

    public static ListingInterface get(Priority priority) {
        return providers.get(priority);
    }

    public static ListingInterface getHighestPriority() {
        if (providers.get(Priority.HIGHEST) != null) {
            return providers.get(Priority.HIGHEST);
        }

        if (providers.get(Priority.HIGH) != null) {
            return providers.get(Priority.HIGH);
        }

        if (providers.get(Priority.MEDIUM) != null) {
            return providers.get(Priority.MEDIUM);
        }

        if (providers.get(Priority.LOW) != null) {
            return providers.get(Priority.LOW);
        }

        if (providers.get(Priority.LOWEST) != null) {
            return providers.get(Priority.LOWEST);
        }

        return null;
    }
}
