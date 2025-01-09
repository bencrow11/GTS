package org.pokesplash.gts.UI.module;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class that creates lore for a listing.
 */
public abstract class ListingInfo {

    /**
     * Creates UI item lore for a given listing.
     * @param listing The listing to create lore for.
     * @return The list of lore created.
     */
    public static List<Component> parse(Listing listing) {
        List<Component> lore = new ArrayList<>();

        lore.add(Component.literal(Gts.language.getSeller() + listing.getSellerName()));
        lore.add(Component.literal(Gts.language.getPrice() + listing.getPriceAsString()));

        if (listing.getEndTime() != -1) {
            lore.add(Component.literal(Gts.language.getRemainingTime() +
                    Utils.parseLongDate(listing.getEndTime() - new Date().getTime())));
        }


        if (!listing.isPokemon()) {
            ItemListing itemListing = (ItemListing) listing;

            // TODO Check for breedable?
//            CompoundTag tag = itemListing.getListing().getTag();
//
//            if (Gts.config.isShowBreedable()) {
//                if (tag != null && tag.contains("breedable")) {
//                    if (tag.getBoolean("breedable")) {
//                        lore.add(Component.literal("§bBreedable"));
//                    } else {
//                        lore.add(Component.literal("§cUnbreedable"));
//                    }
//                }
//            }

            try {
                List<Component> itemTooltips = itemListing.getListing()
                        .getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL);

                lore.addAll(itemTooltips.subList(1, itemTooltips.size()));
            } catch (NullPointerException e) {}
        }


        return lore;
    }
}
