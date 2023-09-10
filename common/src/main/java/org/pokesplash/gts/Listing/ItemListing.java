package org.pokesplash.gts.Listing;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;

import java.util.Date;
import java.util.UUID;

/**
 * Class that holds a single listing.
 */
public class ItemListing {
	// Unique id.
	private final UUID id;
	// The UUID of the person selling the Pokemon.
	private final UUID sellerUuid;
	// The name of the seller.
	private final String sellerName;
	// The price the Pokemon is selling for.
	private final double price;
	// The amount of the item that are listed.
	private final int amount;
	// The time the listing ends.
	private final long endTime;
	// The item that is being listed.
	private final int itemId;

	/**
	 * Constructor to create a new listing.
	 * @param sellerUuid The UUID of the person selling the Pokemon.
	 * @param sellerName The name of the seller.
	 * @param price The price the Pokemon is selling for.
	 * @param amount The amount of items to list.
	 * @param item The item to sell.
	 */
	public ItemListing(UUID sellerUuid, String sellerName, double price, int amount, Item item) {
		this.id = UUID.randomUUID();
		this.sellerUuid = sellerUuid;
		this.sellerName = sellerName;
		this.price = price;
		this.amount = amount;
		this.endTime = new Date().getTime() + (Gts.config.getListing_duration() * 3600000L);
		this.itemId = Item.getId(item); // TODO Get david to do something about nbt.
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

	public int getAmount() {
		return amount;
	}

	public long getEndTime() {
		return endTime;
	}

	public Item getItem() {
		return Item.byId(itemId);
	}
}
