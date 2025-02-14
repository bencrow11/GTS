package org.pokesplash.gts.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.fabric.permission.FabricPermissions;
import org.pokesplash.gts.util.CommandsRegistry;

public class GtsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(CommandsRegistry::registerCommands); // Registers Commands
        ServerWorldEvents.LOAD.register((t, e) -> {
            Gts.server = t;
            Gts.reloadSensitive();
        });
        ServerTickEvents.END_SERVER_TICK.register((e) -> {
            if (e.getTickCount() % Gts.ticksPerCheck == 0) {
                Gts.listings.check();
            }
        });
        Gts.init();
        Gts.permissions = new FabricPermissions();
    }
}
