package org.pokesplash.gts.UI.button;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

public abstract class NextPage {
    public static Button getButton() {
        return LinkedPageButton.builder()
                .linkType(LinkType.Next)
                .display(Utils.parseItemId(Gts.language.getNextPageButtonItems()))
                .with(DataComponents.CUSTOM_NAME, Component.literal(Gts.language.getNextPageButtonLabel()))
                .build();
    }
}
