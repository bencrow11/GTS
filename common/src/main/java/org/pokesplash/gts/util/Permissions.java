package org.pokesplash.gts.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.permission.CobblemonPermission;
import com.cobblemon.mod.common.api.permission.PermissionLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;

/**
 * Class that deals with permission handling.
 */
public class Permissions {
	// Store all created permissions in a hashmap.
	private HashMap<String, CobblemonPermission> permissions;

	/**
	 * Constructor to create a permission object. Add your permissions here using the private method.
	 */
	public Permissions() {
		permissions = new HashMap<>();
		// Add you permissions here.
		createPermission("GtsCommand", "gts.user", 1);
		createPermission("GtsMod", "gts.manage", 2);
		createPermission("GtsReload", "gts.reload", 2);
		createPermission("GtsDebug", "gts.debug", 2);
		createPermission("GtsTimeout", "gts.timeout", 2);
		createPermission("GtsSaveItem", "gts.saveitem", 2);
	}

	/**
	 * Method to add a new permission to the hashmap.
	 * @param name The reference of the permission.
	 * @param permissionNode The permission node.
	 * @param permissionLevel The permission level.
	 */
	private void createPermission(String name, String permissionNode, int permissionLevel) {
		permissions.put(name, new CobblemonPermission(permissionNode, parsePermission(permissionLevel)));
	}

	/**
	 * Method to fetch a permission from its reference.
	 * @param name The reference of the permission.
	 * @return
	 */
	public CobblemonPermission getPermission(String name) {
		return permissions.get(name);
	}

	public boolean hasPermission(ServerPlayer player, CobblemonPermission permission) {
		return Cobblemon.INSTANCE.getPermissionValidator().hasPermission(player, permission);
	}

	private PermissionLevel parsePermission(int permLevel) {
		for (PermissionLevel value : PermissionLevel.values()) {
			if (value.ordinal() == permLevel) {
				return value;
			}
		}
		return PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS;
	}
}
