package org.pokesplash.gts.UI.button;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.Page;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.UI.ItemListings;
import org.pokesplash.gts.enumeration.Sort;
import org.pokesplash.gts.util.Utils;

public abstract class SeeItemListings {
    public static Button getButton() {
        return GooeyButton.builder()
                .display(Utils.parseItemId(Gts.language.getItemListingsButtonItem()))
                .with(DataComponents.CUSTOM_NAME,
                        Component.literal(Gts.language.getItemListingsButtonLabel()))
                .onClick((action) -> {
                    ServerPlayer sender = action.getPlayer();
                    Page page = new ItemListings().getPage(Sort.NONE);
                    UIManager.openUIForcefully(sender, page);
                })
                .build();
    }
}
