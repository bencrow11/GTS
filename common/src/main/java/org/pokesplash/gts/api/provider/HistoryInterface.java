package org.pokesplash.gts.api.provider;

import org.pokesplash.gts.history.HistoryItem;

/**
 * Interface to create new history providers.
 */
public interface HistoryInterface {
    public void write(HistoryItem listing);
}
