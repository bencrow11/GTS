package org.pokesplash.gts.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.CommandsRegistry;

public class GtsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Gts.init();
        CommandRegistrationCallback.EVENT.register(CommandsRegistry::registerCommands); // Registers Commands
        // TODO Remove?
//        ServerLifecycleEvents.SERVER_STOPPING.register((e) -> { // Removes all timers when the server stops.
//            Gts.timers.deleteAllTimers();
//        });
        ServerWorldEvents.UNLOAD.register((e, f) -> { // Removes all timers when the server stops.
            Gts.timers.deleteAllTimers();
        });
        ServerWorldEvents.LOAD.register((t, e) -> Gts.server = t);
    }
}
