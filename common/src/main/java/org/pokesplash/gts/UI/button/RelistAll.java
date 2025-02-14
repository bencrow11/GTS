package org.pokesplash.gts.UI.button;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.Page;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.UI.AllListings;
import org.pokesplash.gts.util.ColorUtil;

public abstract class RelistAll {
    public static Button getButton() {
        return GooeyButton.builder()
                .display(Gts.language.getRelistExpiredButtonItem())
                .with(DataComponents.CUSTOM_NAME,
                        ColorUtil.parse(Gts.language.getRelistExpiredButtonLabel()))
                .onClick((action) -> {
                    ServerPlayer sender = action.getPlayer();
                    Gts.listings.relistAllExpiredListings(sender.getUUID());
                    Page page = new AllListings().getPage();
                    UIManager.openUIForcefully(sender, page);
                })
                .build();
    }
}
