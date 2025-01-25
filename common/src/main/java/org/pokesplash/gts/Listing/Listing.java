package org.pokesplash.gts.Listing;

import com.google.gson.Gson;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.api.provider.ListingAPI;
import org.pokesplash.gts.util.Utils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Used for both Pokemon and Item listings.
 * @param <T> The type of object returned from getListing(), either ItemStack or Pokemon.
 */
public abstract class Listing<T> {

    protected String version = Gts.LISTING_FILE_VERSION;
    // The unique ID of the listing.
    protected UUID id;
    // The UUID of the person selling the Pokemon.
    private UUID sellerUuid;
    // The name of the seller.
    private String sellerName;
    // The price the Pokemon is selling for.
    private double price;
    // The time the listing ends.
    private long endTime;
    // Is the listings a Pokemon?
    private boolean isPokemon;

    public Listing(UUID sellerUuid, String sellerName, double price, boolean isPokemon) {
        this.id = UUID.randomUUID();
        this.sellerUuid = sellerUuid;
        this.sellerName = sellerName;
        this.price = price;

        // If debug mode, set timer to 1 minute.
        if (Gts.isDebugMode) {
            this.endTime = new Date().getTime() + (1000L * Gts.debugTime);
        // If duration is less than 0, no listing timer.
        } else if (Gts.config.getListingDuration() <= 0) {
            this.endTime = -1;
        // Otherwise set the timer to the listing duration.
        } else {
            this.endTime = new Date().getTime() + (Gts.config.getListingDuration() * 3600000L);
        }

        this.isPokemon = isPokemon;
    }

    /**
     * Helps differentiate Item Listings from Pokemon Listings.
     * @return true if the listing is a Pokemon listing.
     */
    public boolean isPokemon() {
        return isPokemon;
    }

    /**
     * Gets the file version.
     * @return The file version.
     */
    public String getVersion() {
        return version;
    }

	public UUID getId() { // UUID of the listing.
		return id;
	};

    public UUID getSellerUuid()  // UUID of the seller.
    {
        return sellerUuid;
    }

    public String getSellerName() // Name of the seller.
    {
        return sellerName;
    }

    public double getPrice() // Price of the listing.
    {
        return price;
    }

    public String getPriceAsString() {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(price);
    }

    public long getEndTime() // End time of the listing.
    {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Renews the end time. Used when relisting an expired listing.
     */
    public void renewEndTime() {
        // If debug mode, set timer to 1 minute.
        if (Gts.isDebugMode) {
            this.endTime = new Date().getTime() + (1000L * Gts.debugTime);
            // If duration is less than 0, no listing timer.
        } else if (Gts.config.getListingDuration() <= 0) {
            this.endTime = -1;
            // Otherwise set the timer to the listing duration.
        } else {
            this.endTime = new Date().getTime() + (Gts.config.getListingDuration() * 3600000L);
        }
    }

    public abstract T getListing(); // The object that has been listed.

    public abstract boolean isListingValid(); // Checks the listing is present.

    public boolean write(String filePath) { // Writes the listing to file.

        if (ListingAPI.getHighestPriority() != null) {
            ListingAPI.getHighestPriority().write(this);
            return true;
        }

        Gson gson = Utils.newGson();
        String data = gson.toJson(this);

        CompletableFuture<Boolean> future = Utils.writeFileAsync(filePath, this.getId() + ".json", data);

        return future.join();
    }

    public boolean delete(String filePath) { // Deletes the listing file.

        if (ListingAPI.getHighestPriority() != null) {
            ListingAPI.getHighestPriority().delete(this);
            return true;
        }

        return Utils.deleteFile(filePath, this.getId() + ".json");
    }

    public void update(boolean isPokemon) {
        this.version = Gts.LISTING_FILE_VERSION;
        this.isPokemon = isPokemon;
    }

    public abstract Listing deepClone();

    public abstract String getListingName();

    @Override
    public boolean equals(Object o) {
        if (o instanceof Listing) {
            return ((Listing<?>) o).getId().equals(this.getId());
        }
        return false;
    }

    public abstract MutableComponent getDisplayName();

    public abstract String getUiTitle();

    public abstract ItemStack getIcon();
}
