package org.pokesplash.gts.history;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

/**
 * A class that is used to store a previously sold item.
 * @param <T> The type of item that is being sold (Pokemon, ItemStack).
 */
public abstract class HistoryItem<T> {
    private boolean isPokemon; // Is the listings a Pokemon.
    private UUID id; // The listings ID.
    private UUID sellerUuid; // The seller UUID.
    private String sellerName; // The name of the seller.
    private double price; // The price the listings sold for.
    private long soldDate; // The date the listings was sold.
    private String buyerName; // The name of the person who bought it.


    public HistoryItem(boolean isPokemon, UUID sellerUuid, String sellerName, double price, String buyerName) {
        this.isPokemon = isPokemon;
        this.id = UUID.randomUUID();
        this.sellerUuid = sellerUuid;
        this.sellerName = sellerName;
        this.price = price;
        this.soldDate = new Date().getTime();
        this.buyerName = buyerName;
    }

    public boolean isPokemon() {
        return isPokemon;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSellerUuid() {
        return sellerUuid;
    }

    public String getSellerName() {
        return sellerName;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceAsString() {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(price);
    }

    public long getSoldDate() {
        return soldDate;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public abstract T getListing(); // The object that has been listed.
}
