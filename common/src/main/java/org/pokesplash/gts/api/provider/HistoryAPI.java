package org.pokesplash.gts.api.provider;

import java.util.HashMap;
import java.util.Map;

public abstract class HistoryAPI {
    private static Map<Priority, HistoryInterface> providers = new HashMap<>();

    public static void add(Priority priority, HistoryInterface provider) {
        providers.put(priority, provider);
    }

    public static void remove(Priority priority) {
        providers.remove(priority);
    }

    public static Map<Priority, HistoryInterface> getProviders() {
        return providers;
    }

    public static HistoryInterface get(Priority priority) {
        return providers.get(priority);
    }

    public static HistoryInterface getHighestPriority() {
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
