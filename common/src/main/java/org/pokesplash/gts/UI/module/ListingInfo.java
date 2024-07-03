package org.pokesplash.gts.UI.module;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.ItemListing;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public abstract class ListingInfo {
    public static Collection<Component> parse(Listing listing) {
        Collection<Component> lore = new ArrayList<>();

        lore.add(Component.literal(Gts.language.getSeller() + listing.getSellerName()));
        lore.add(Component.literal(Gts.language.getPrice() + listing.getPriceAsString()));

        if (listing.getEndTime() != -1) {
            lore.add(Component.literal(Gts.language.getRemainingTime() +
                    Utils.parseLongDate(listing.getEndTime() - new Date().getTime())));
        }

        if (!listing.isPokemon()) {
            ItemListing itemListing = (ItemListing) listing;
            CompoundTag tag = itemListing.getListing().getTag();

            if (Gts.config.isShowBreedable()) {
                if (tag != null && tag.contains("breedable")) {
                    if (tag.getBoolean("breedable")) {
                        lore.add(Component.literal("§bBreedable"));
                    } else {
                        lore.add(Component.literal("§cUnbreedable"));
                    }
                }
            }

            List<Component> itemTooltips = itemListing.getListing().getTooltipLines(null, TooltipFlag.NORMAL);

            lore.addAll(itemTooltips.subList(1, itemTooltips.size()));
        }

        return lore;
    }
}
