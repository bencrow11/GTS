package org.pokesplash.gts.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.pokesplash.gts.Gts;

public class GtsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Gts.init();
    }
}