package org.pokesplash.gts.config;

public class PokemonPrices {
    private PokemonAspects pokemon;
    private double price;

    public PokemonPrices() {
        pokemon = new PokemonAspects();
        price = 10000;
    }

    public PokemonAspects getPokemon() {
        return pokemon;
    }

    public double getPrice() {
        return price;
    }
}
