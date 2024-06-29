package org.pokesplash.gts.UI.button;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;

public abstract class Filler {
    public static Button getButton() {
        return GooeyButton.builder()
                .display(Utils.parseItemId(Gts.language.getFillerItem()))
                .hideFlags(FlagType.All)
                .lore(new ArrayList<>())
                .title("")
                .build();
    }
}
