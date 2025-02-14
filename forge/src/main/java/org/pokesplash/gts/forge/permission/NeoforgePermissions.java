package org.pokesplash.gts.forge.permission;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import org.pokesplash.gts.permission.GtsPermission;
import org.pokesplash.gts.permission.PermissionProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NeoforgePermissions extends PermissionProvider {

    private HashMap<String, PermissionNode<Boolean>> nodes;

    public NeoforgePermissions() {
        nodes = new HashMap<>();
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String base) {
        GtsPermission permission = super.getPermission(base);
        return PermissionAPI.getPermission(player, nodes.get(permission.getNode()));
    }

    public List<PermissionNode<Boolean>> createNodes() {
        super.getPermissions().values().forEach(permissionNode -> {
            nodes.put(permissionNode.getNode(), new PermissionNode<Boolean>("gts",
                    permissionNode.getNode().replace("gts.", ""), PermissionTypes.BOOLEAN,
                    (player, uuid, context) ->
                            player.hasPermissions(permissionNode.getLevel())
                    ));
        });
        return new ArrayList<>(nodes.values());
    }
}
