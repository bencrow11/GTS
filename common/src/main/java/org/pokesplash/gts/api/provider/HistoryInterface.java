package org.pokesplash.gts.api.provider;

import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.history.HistoryItem;

public interface HistoryInterface {
    public void write(HistoryItem listing);
}
