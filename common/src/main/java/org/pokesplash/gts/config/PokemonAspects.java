package org.pokesplash.gts.config;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class PokemonAspects {
    private String species;
    private String form;
    private String ability;
    private String gender;

    public PokemonAspects() {
        this.species = "blaziken";
        this.form = "";
        this.ability = "speedboost";
        this.gender = "male";
    }

    public PokemonAspects(String species) {
        this.species = species;
        this.form = "";
        this.ability = "";
        this.gender = "";
    }

    public String getSpecies() {
        return species;
    }

    public String getForm() {
        return form;
    }

    public String getAbility() {
        return ability;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof PokemonAspects) {
            PokemonAspects other = (PokemonAspects) o;
            return species.equals(other.getSpecies()) && form.equals(other.getForm())
                    && ability.equals(other.getAbility()) && gender.equals(other.getGender());
        }

        if (o instanceof Pokemon) {
            Pokemon other = (Pokemon) o;

            boolean isSameSpecies = false;
            boolean isSameForm = false;
            boolean isSameAbility = false;
            boolean isSameGender = false;

            if (species.trim().isBlank() || species.equalsIgnoreCase(other.getSpecies().getName())) {
                isSameSpecies = true;
            }

            if (form.trim().isBlank() || form.equalsIgnoreCase(other.getForm().getName())) {
                isSameForm = true;
            }

            if (ability.trim().isBlank() || ability.equalsIgnoreCase(other.getAbility().getName())) {
                isSameAbility = true;
            }

            if (gender.trim().isBlank() || gender.equalsIgnoreCase(other.getGender().name())) {
                isSameGender = true;
            }

            return isSameSpecies && isSameForm && isSameAbility && isSameGender;
        }

        return false;
    }
}
