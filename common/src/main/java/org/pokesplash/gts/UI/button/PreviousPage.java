package org.pokesplash.gts.UI.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

public abstract class PreviousPage {
    public static Button getButton() {
        return LinkedPageButton.builder()
                .display(Utils.parseItemId(Gts.language.getPreviousPageButtonItems()))
                .linkType(LinkType.Previous)
                .with(DataComponents.CUSTOM_NAME,
                        Component.literal(Gts.language.getPreviousPageButtonLabel()))
                .build();
    }
}
