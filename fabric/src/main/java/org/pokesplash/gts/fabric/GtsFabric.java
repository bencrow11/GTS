package org.pokesplash.gts.fabric;

import net.fabricmc.api.ModInitializer;
import org.pokesplash.gts.Gts;

public class GtsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Gts.init();
    }
}
