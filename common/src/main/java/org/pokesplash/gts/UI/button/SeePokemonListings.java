package org.pokesplash.gts.UI.button;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.Page;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.UI.PokemonListings;
import org.pokesplash.gts.enumeration.Sort;
import org.pokesplash.gts.util.ColorUtil;

public abstract class SeePokemonListings {
    public static Button getButton() {
        return GooeyButton.builder()
                .display(Gts.language.getPokemonListingsButtonItem())
                .with(DataComponents.CUSTOM_NAME,
                        ColorUtil.parse(Gts.language.getPokemonListingsButtonLabel()))
                .with(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
                .onClick((action) -> {
                    ServerPlayer sender = action.getPlayer();
                    Page page = new PokemonListings().getPage(Sort.NONE);
                    UIManager.openUIForcefully(sender, page);
                })
                .build();
    }
}
