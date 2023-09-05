package org.pokesplash.gts.fabric;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.pokesplash.gts.Gts;

public class PlayerJoinEvent implements ServerPlayConnectionEvents.Join {
	@Override
	public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		Gts.listings.playerJoinedEvent(handler.getPlayer());
	}
}
