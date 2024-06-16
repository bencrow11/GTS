package org.pokesplash.gts.api.provider;

import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ListingsProvider;
import org.pokesplash.gts.history.HistoryProvider;

import java.util.HashMap;
import java.util.Map;

public abstract class HistoryProviderAPI {
    private static HashMap<Priority, HistoryProvider> providers = new HashMap<>();

    public static void add(Priority priority, HistoryProvider provider) {
        providers.put(priority, provider);
        Gts.history = getHighestPriority();
        Gts.history.init();
    }

    public static void remove(Priority priority) {
        providers.remove(priority);
        Gts.history = getHighestPriority();
        Gts.history.init();
    }

    public static Map<Priority, HistoryProvider> getProviders() {
        return providers;
    }

    public static HistoryProvider get(Priority priority) {
        return providers.get(priority);
    }

    public static HistoryProvider getHighestPriority() {
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

        if (providers.get(Priority.LOW) != null) {
            return providers.get(Priority.LOWEST);
        }

        return new HistoryProvider();
    }
}
