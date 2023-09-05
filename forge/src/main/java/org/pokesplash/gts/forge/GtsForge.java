package org.pokesplash.gts.forge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import org.pokesplash.gts.Gts;

@Mod(Gts.MOD_ID)
public class GtsForge {
    public GtsForge() {
        Gts.init();
    }

    private void PlayerJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        Gts.listings.playerJoinedEvent((ServerPlayer) event.getEntity());
    }
}