package org.pokesplash.gts.UI.module;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.util.ColorUtil;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

        Style base = Style.EMPTY.withItalic(false);

        lore.add(ColorUtil.parse(Gts.language.getSeller() + listing.getSellerName()));
        lore.add(ColorUtil.parse(Gts.language.getPrice() + listing.getPriceAsString()));

        if (listing.getEndTime() != -1 && listing.getEndTime() > new Date().getTime()) {
            lore.add(ColorUtil.parse(Gts.language.getRemainingTime() +
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
            if (!itemListing.getListing().getItem().getDescriptionId().contains("travelersbackpack")) {
                try {
                    List<Component> itemTooltips = itemListing.getListing()
                            .getTooltipLines(Item.TooltipContext.EMPTY, null, TooltipFlag.NORMAL);

                    lore.addAll(itemTooltips.subList(1, itemTooltips.size()));
                } catch (Exception e) {}
            }
        }


        return lore;
    }
}
