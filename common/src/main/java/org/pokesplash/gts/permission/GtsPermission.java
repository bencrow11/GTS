package org.pokesplash.gts.permission;

public class GtsPermission {
    private String name;
    private String node;
    private int level;

    public GtsPermission(String name, String node, int level) {
        this.name = name;
        this.node = node;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getNode() {
        return node;
    }

    public int getLevel() {
        return level;
    }
}
