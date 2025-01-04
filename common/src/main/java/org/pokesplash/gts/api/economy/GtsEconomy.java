package org.pokesplash.gts.api.economy;

import java.util.UUID;

public interface GtsEconomy {
    boolean add(UUID player, double amount);

    boolean remove(UUID player, double amount);
}
