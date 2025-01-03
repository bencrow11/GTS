package org.pokesplash.gts.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;

public abstract class CodecUtils {

    public static JsonElement encodeItem(ItemStack stack) {
        return ItemStack.CODEC.encodeStart(Gts.server.registryAccess().createSerializationContext(JsonOps.INSTANCE),
                stack).getOrThrow();
    }

    public static ItemStack decodeItem(JsonElement json) {
        return ItemStack.CODEC.decode(Gts.server.registryAccess().createSerializationContext(JsonOps.INSTANCE),
                json).getOrThrow().getFirst();
    }
}
