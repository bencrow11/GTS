package org.pokesplash.gts.Listing;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * Class that holds a single listing.
 */
public class ItemListing extends Listing<ItemStack> {
	// The item that is being listed.
	private final String item;

	/**
	 * Constructor to create a new listing.
	 * @param sellerUuid The UUID of the person selling the Pokemon.
	 * @param sellerName The name of the seller.
	 * @param price The price the Pokemon is selling for.
	 * @param item The item to sell.
	 */
	public ItemListing(UUID sellerUuid, String sellerName, double price, ItemStack item) {
		super(sellerUuid, sellerName, price, false);
		this.item = item.save(HolderLookup.Provider.create(Stream.empty())).getAsString();
	}

	public ItemListing(ItemListing other) {
		super(UUID.fromString(other.getSellerUuid().toString()),
				String.copyValueOf(other.getSellerName().toCharArray()), other.getPrice(), false);
		this.item = other.getListing().save(HolderLookup.Provider.create(Stream.empty())).getAsString();
		super.id = UUID.fromString(other.getId().toString());
		super.version = String.copyValueOf(other.getVersion().toCharArray());
		super.setEndTime(other.getEndTime());
	}

	@Override
	public ItemStack getListing() {
		try {
			ItemStack.parse(HolderLookup.Provider.create(Stream.empty()), TagParser.parseTag(item));
		} catch (CommandSyntaxException e) {
			Gts.LOGGER.fatal("Failed to parse item for NBT: " + item);
			Gts.LOGGER.fatal("Stacktrace: ");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Listing deepClone() {
		return new ItemListing(this);
	}

	@Override
	public String getListingName() {
		return getListing().getDisplayName().getString()
				.replaceAll("\\[", "")
				.replaceAll("\\]", "");
	}
}
