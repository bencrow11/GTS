package org.pokesplash.gts.forge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.pokesplash.gts.Gts;
import org.pokesplash.gts.util.CommandsRegistry;

@Mod(Gts.MOD_ID)
public class GtsForge {
    public GtsForge() {
        Gts.init();
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandsRegistry.registerCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public void test(LevelEvent.Unload event) { Gts.server = event.getLevel().getServer();
    }

    @SubscribeEvent
    public void worldLoadEvent(LevelEvent.Load event) {
        Gts.server = event.getLevel().getServer();
    }

    @SubscribeEvent
    public void tickEvent(ServerTickEvent event) {
        Gts.listings.check();
    }

}