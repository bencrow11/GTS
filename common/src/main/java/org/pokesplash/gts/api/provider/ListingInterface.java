package org.pokesplash.gts.api.provider;

import org.pokesplash.gts.Listing.Listing;

public interface ListingInterface {
    public void write(Listing listing);
    public void delete(Listing listing);
    public void update(Listing listing);
}
