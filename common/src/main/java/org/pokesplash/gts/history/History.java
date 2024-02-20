package org.pokesplash.gts.history;

import java.util.UUID;

public interface History {
    String version(); // The current version of the file.
    UUID getPlayer(); // The players UUID.
}
