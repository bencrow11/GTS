package org.pokesplash.gts.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.CommandsRegistry;
import org.pokesplash.gts.util.Utils;

public class GtsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Gts.init();
        CommandRegistrationCallback.EVENT.register(CommandsRegistry::registerCommands); // Registers Commands
        ServerLifecycleEvents.SERVER_STOPPING.register((e) -> { // Removes all timers when the server stops.
            Utils.removeAllTimers();
        });
        ServerWorldEvents.LOAD.register((t, e) -> Gts.server = t);
    }
}
