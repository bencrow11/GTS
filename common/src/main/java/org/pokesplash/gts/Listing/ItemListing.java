package org.pokesplash.gts.Listing;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;

import java.util.UUID;

/**
 * Class that holds a single listing.
 */
public class ItemListing extends Listing<ItemStack> {
	// The item that is being listed.
	private final JsonElement item;

	/**
	 * Constructor to create a new listing.
	 * @param sellerUuid The UUID of the person selling the Pokemon.
	 * @param sellerName The name of the seller.
	 * @param price The price the Pokemon is selling for.
	 * @param item The item to sell.
	 */
	public ItemListing(UUID sellerUuid, String sellerName, double price, ItemStack item) {
		super(sellerUuid, sellerName, price, false);
		this.item = ItemStack.CODEC.encodeStart(
				Gts.server.registryAccess().createSerializationContext(JsonOps.INSTANCE), item).getOrThrow();
	}

	public ItemListing(ItemListing other) {
		super(UUID.fromString(other.getSellerUuid().toString()),
				String.copyValueOf(other.getSellerName().toCharArray()), other.getPrice(), false);
		this.item = ItemStack.CODEC.encodeStart(Gts.server.registryAccess().createSerializationContext(JsonOps.INSTANCE),
				other.getListing()).getOrThrow();
		super.id = UUID.fromString(other.getId().toString());
		super.version = String.copyValueOf(other.getVersion().toCharArray());
		super.setEndTime(other.getEndTime());
	}

	@Override
	public ItemStack getListing() {
		try {
			ItemStack itemStack = ItemStack.CODEC.decode(Gts.server.registryAccess().createSerializationContext(JsonOps.INSTANCE),
					item).getOrThrow().getFirst();
			return itemStack;
		} catch (IllegalStateException e) {
			Gts.LOGGER.fatal("Failed to parse item for " + item);
			Gts.LOGGER.fatal("Stacktrace: ");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isListingValid() {
        return item != null && item.isJsonObject();
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

	@Override
	public MutableComponent getDisplayName() {
		Style dark_aqua = Style.EMPTY.withItalic(false).withColor(TextColor.parseColor("dark_aqua").getOrThrow());
		return Component.empty().setStyle(dark_aqua).append(getListing().getHoverName());
	}

	@Override
	public String getUiTitle() {
		return Gts.language.getItemTitle();
	}

	@Override
	public ItemStack getIcon() {
		return getListing();
	}
}
