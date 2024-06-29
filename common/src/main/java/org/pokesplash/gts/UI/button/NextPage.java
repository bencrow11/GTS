package org.pokesplash.gts.UI.button;

import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

public abstract class NextPage {
    public static LinkedPageButton getButton() {
        return LinkedPageButton.builder()
                .display(Utils.parseItemId(Gts.language.getNextPageButtonItems()))
                .title(Gts.language.getNextPageButtonLabel())
                .linkType(LinkType.Next)
                .build();
    }
}
