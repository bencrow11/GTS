package org.pokesplash.gts.config;

import com.cobblemon.mod.common.CobblemonItems;
import com.google.gson.JsonElement;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.oldVersion.ItemPricesOld;
import org.pokesplash.gts.util.CodecUtils;
import org.pokesplash.gts.util.Utils;

/**
 * A single item price for the config.
 */
public class ItemPrices {
	// The item id.
	private JsonElement item;

	// The price of the item.
	private double price;

	/**
	 * Constructor to create an example item.
	 */
	public ItemPrices() {
		item = CodecUtils.encodeItem(new ItemStack(CobblemonItems.ASSAULT_VEST));
		price = 10000;
	}

	public ItemPrices(ItemPricesOld old) {
		item = CodecUtils.encodeItem(Utils.parseItemId(old.getItem_name()));
		price = old.getMin_price();
	}

	public JsonElement getItem() {
		return item;
	}

	/**
	 * Getter for the minimum price of the item.
	 * @return min price as an int.
	 */
	public double getMinPrice() {
		return price;
	}
}
