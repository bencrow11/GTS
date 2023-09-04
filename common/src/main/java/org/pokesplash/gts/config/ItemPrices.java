package org.pokesplash.gts.config;

/**
 * A single item price for the config.
 */
public class ItemPrices {
	// The item id.
	private String item_name;

	// The price of the item.
	private int min_price;

	/**
	 * Constructor to create an example item.
	 */
	public ItemPrices() {
		item_name = "cobblemon:assault_vest";
		min_price = 10000;
	}

	/**
	 * Getter for the item name.
	 * @return item name as a String.
	 */
	public String getItem_name() {
		return item_name;
	}

	/**
	 * Getter for the minimum price of the item.
	 * @return min price as an int.
	 */
	public int getMin_price() {
		return min_price;
	}
}
