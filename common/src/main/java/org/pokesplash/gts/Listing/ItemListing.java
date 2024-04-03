package org.pokesplash.gts.Listing;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

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
		this.item = item.save(new CompoundTag()).getAsString();
	}

	@Override
	public ItemStack getListing() {
		try {
			return ItemStack.of(TagParser.parseTag(item));
		} catch (CommandSyntaxException e) {
			Gts.LOGGER.fatal("Failed to parse item for NBT: " + item);
			Gts.LOGGER.fatal("Stacktrace: ");
			e.printStackTrace();
		}
		return null;
	}
}
