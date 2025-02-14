package org.pokesplash.gts.permission;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;

/**
 * Class that stores all of the GTS permissions.
 */
public abstract class PermissionProvider {

    private HashMap<String, GtsPermission> permissions;

    public PermissionProvider() {
        permissions = new HashMap<>();
        permissions.put("base", new GtsPermission("base", "gts.user.base", 0));
        permissions.put("debug", new GtsPermission("debug", "gts.admin.debug", 3));
        permissions.put("expired", new GtsPermission("expired", "gts.user.expired", 0));
        permissions.put("getprice", new GtsPermission("getprice", "gts.user.price", 0));
        permissions.put("history", new GtsPermission("history", "gts.user.history", 0));
        permissions.put("sell", new GtsPermission("sell", "gts.user.sell", 0));
        permissions.put("manage", new GtsPermission("manage", "gts.user.manage", 0));
        permissions.put("reload", new GtsPermission("reload", "gts.admin.reload", 3));
        permissions.put("saveitem", new GtsPermission("saveitem", "gts.admin.saveitem", 3));
        permissions.put("search", new GtsPermission("search", "gts.user.search", 0));
        permissions.put("timeout", new GtsPermission("timeout", "gts.moderation.timeout", 2));
        permissions.put("remove", new GtsPermission("remove", "gts.moderation.remove", 2));
    }

    public HashMap<String, GtsPermission> getPermissions() {
        return permissions;
    }

    public GtsPermission getPermission(String permission) {
        return permissions.get(permission);
    }

    public abstract boolean hasPermission(ServerPlayer player, String base);
}
