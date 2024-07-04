package org.pokesplash.gts.config;

/**
 * Class used for button materials
 */
public class Material {
    private String material; // The material ID
    private String nbt; // The material NBT

    /**
     * Constructor to create a new material object.
     * @param material The material ID.
     * @param nbt The NBT of the material.
     */
    public Material(String material, String nbt) {
        this.material = material;
        this.nbt = nbt;
    }

    /**
     * Getters
     */

    public String getMaterial() {
        return material;
    }

    public String getNBT() {
        return nbt;
    }
}
