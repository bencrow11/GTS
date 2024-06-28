package org.pokesplash.gts.history;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;

/**
 * Class that is used to save sold Items.
 */
public class ItemHistoryItem extends HistoryItem<ItemStack> {

    private String item;

    public ItemHistoryItem(ItemListing listing, String buyerName) {
        super(listing.isPokemon(), listing.getSellerUuid(), listing.getSellerName(), listing.getPrice(), buyerName);
        this.item = listing.getListing().save(new CompoundTag()).getAsString();
    }

    /**
     * Method to get the Item that has been sold.
     * @return The Item that has been sold.
     */
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
