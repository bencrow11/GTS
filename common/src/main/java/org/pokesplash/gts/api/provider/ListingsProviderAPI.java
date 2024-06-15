package org.pokesplash.gts.api.provider;

import org.pokesplash.gts.Listing.ListingsProvider;

import java.util.Map;

public abstract class ListingsProviderAPI {
    private static Map<Priority, ListingsProvider> providers = Map.of(Priority.LOWEST, new ListingsProvider());

    public static void add(Priority priority, ListingsProvider provider) {
        providers.put(priority, provider);
    }

    public static void remove(Priority priority) {
        if (priority != Priority.LOWEST) {
            providers.remove(priority);
        }
    }

    public static Map<Priority, ListingsProvider> getProviders() {
        return providers;
    }

    public static ListingsProvider get(Priority priority) {
        return providers.get(priority);
    }

    public static ListingsProvider getHighestPriority() {
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

        return providers.get(Priority.LOWEST);
    }
}
