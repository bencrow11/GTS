package org.pokesplash.gts.Listing;

import java.util.UUID;

/**
 * Used for both Pokemon and Item listings.
 * @param <T> The type of object returned from getListing(), either ItemStack or Pokemon.
 */
public abstract class Listing<T> {

	public UUID getId() { // UUID of the listing.
		return null;
	};

    public UUID getSellerUuid()  // UUID of the seller.
    {
        return null;
    }

    public String getSellerName() // Name of the seller.
    {
        return null;
    }

    public double getPrice() // Price of the listing.
    {
        return 0;
    }

    public String getPriceAsString() // Price of the listing as String.
    {
        return null;
    }

    public long getEndTime() // End time of the listing.
    {
        return 0;
    }

    public T getListing() // The object that has been listed.
    {
        return null;
    }

    public boolean isPokemon() // Checks that the listing is a pokemon;
    {
        return false;
    }
}
