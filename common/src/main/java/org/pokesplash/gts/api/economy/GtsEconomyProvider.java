package org.pokesplash.gts.api.economy;

import org.pokesplash.gts.enumeration.Priority;
import org.pokesplash.gts.impactor.ImpactorService;

import java.util.HashMap;

public abstract class GtsEconomyProvider {
    private static HashMap<Priority, GtsEconomy> providers = new HashMap<>();

    public static void putEconomy(Priority priority, GtsEconomy economy) {
        providers.put(priority, economy);
    }

    public static GtsEconomy getEconomy(Priority priority) {
        return providers.get(priority);
    }

    public static GtsEconomy getHighestEconomy() {
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

        return new ImpactorService();
    }
}
