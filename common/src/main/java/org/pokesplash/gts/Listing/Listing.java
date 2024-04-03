package org.pokesplash.gts.Listing;

import com.google.gson.Gson;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Used for both Pokemon and Item listings.
 * @param <T> The type of object returned from getListing(), either ItemStack or Pokemon.
 */
public abstract class Listing<T> {

    private String version = Gts.LISTING_FILE_VERSION;
    private boolean isPokemon;


    public Listing(boolean isPokemon) {
        this.isPokemon = isPokemon;
    }

    public boolean isPokemon() {
        return isPokemon;
    }

    public String getVersion() {
        return version;
    }

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

    public boolean write(String filePath) { // Writes the listing to file.
        Gson gson = Utils.newGson();
        String data = gson.toJson(this);

        CompletableFuture<Boolean> future = Utils.writeFileAsync(filePath, this.getId() + ".json", data);

        return future.join();
    }

    public boolean delete(String filePath) { // Deletes the listing file.
        return Utils.deleteFile(filePath, this.getId() + ".json");
    }

    public void update(boolean isPokemon) {
        this.version = Gts.LISTING_FILE_VERSION;
        this.isPokemon = isPokemon;
    }
}
