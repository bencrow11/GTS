package org.pokesplash.gts.permission;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.server.level.ServerPlayer;

public abstract class LuckPermsUtils {

    /**
     * Checks a user has a given permission.
     * @param user The user to check the permission on.
     * @param permission The permission to check the user has.
     * @return true if the user has the permission.
     */
    public static boolean hasPermission(ServerPlayer user, String permission) {
        User playerLP = LuckPermsProvider.get().getUserManager().getUser(user.getUUID());

        if (playerLP == null) {
            System.out.println("Could not find player " + user.getUUID() + " in LuckPerms for GTS.");
            return false;
        }

        return playerLP.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
