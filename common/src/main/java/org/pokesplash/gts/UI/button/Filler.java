package org.pokesplash.gts.UI.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import org.pokesplash.gts.Gts;

public abstract class Filler {
    public static Button getButton() {
        return GooeyButton.builder()
                .display(Gts.language.getFillerItem())
                .with(DataComponents.HIDE_TOOLTIP, Unit.INSTANCE)
                .with(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
                .with(DataComponents.CUSTOM_NAME, Component.empty())
                .build();
    }
}
