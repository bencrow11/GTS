package org.pokesplash.gts.api.economy;

import java.util.UUID;

/**
 * Interface used for GTS to interact with an economy mod.
 */
public interface GtsEconomy {
    boolean add(UUID player, double amount);

    boolean remove(UUID player, double amount);

    double balance(UUID player);
}
