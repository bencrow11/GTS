package org.pokesplash.gts.api.economy;

import org.pokesplash.gts.enumeration.Priority;
import org.pokesplash.gts.impactor.ImpactorService;

import java.util.HashMap;

/**
 * Class that stores the economy mod class used to process transactions.
 */
public abstract class GtsEconomyProvider {

    // List of providers and their priorities.
    private static HashMap<Priority, GtsEconomy> providers = new HashMap<>();

    // Used for economy mods to insert an economy bridge to use.
    public static void putEconomy(Priority priority, GtsEconomy economy) {
        providers.put(priority, economy);
    }

    // Gets an economy provider using a given priority.
    public static GtsEconomy getEconomy(Priority priority) {
        return providers.get(priority);
    }

    // Finds the economy provider with the highest priority.
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

        // If no priority was found, default to Impactor.
        return new ImpactorService();
    }
}
