package org.pokesplash.gts.UI.module;

import net.minecraft.network.chat.Component;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.Listing.Listing;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class ListingInfo {
    public static Collection<Component> parse(Listing listing) {
        Collection<Component> lore = new ArrayList<>();

        lore.add(Component.literal(Gts.language.getSeller() + listing.getSellerName()));
        lore.add(Component.literal(Gts.language.getPrice() + listing.getPriceAsString()));

        if (listing.getEndTime() != -1) {
            lore.add(Component.literal(Gts.language.getRemainingTime() +
                    Utils.parseLongDate(listing.getEndTime() - new Date().getTime())));
        }

        return lore;
    }
}
