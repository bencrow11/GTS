package org.pokesplash.gts.permission;

import java.util.HashMap;

/**
 * Class that stores all of the GTS permissions.
 */
public class PermissionProvider {

    private HashMap<String, String> permissions;

    public PermissionProvider() {
        permissions = new HashMap<>();
        permissions.put("base", "gts.user.base");
        permissions.put("debug", "gts.admin.debug");
        permissions.put("expired", "gts.user.expired");
        permissions.put("getprice", "gts.user.price");
        permissions.put("history", "gts.user.history");
        permissions.put("sell", "gts.user.sell");
        permissions.put("manage", "gts.user.manage");
        permissions.put("reload", "gts.admin.reload");
        permissions.put("saveitem", "gts.admin.saveitem");
        permissions.put("search", "gts.user.search");
        permissions.put("timeout", "gts.moderation.timeout");
        permissions.put("remove", "gts.moderation.remove");
    }

    public String getPermission(String permission) {
        return permissions.get(permission);
    }
}
