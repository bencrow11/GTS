package org.pokesplash.gts.UI.module;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class PokemonInfo {
	public static Collection<String> parse(PokemonListing listing) {
		Collection<String> lore = new ArrayList<>();

		lore.add("§2Nature: §a" + Utils.capitaliseFirst(listing.getPokemon().getNature().getName().toString().split(":")[1]));

		if (Utils.isHA(listing.getPokemon())) {
			lore.add("§2Ability: §a" + Utils.capitaliseFirst(listing.getPokemon().getAbility().getName()) + " §b(HA)");
		} else {
			lore.add("§2Ability: §a" + Utils.capitaliseFirst(listing.getPokemon().getAbility().getName()));
		}

		lore.add("§2Level: §a" + listing.getPokemon().getLevel());
		lore.add("§2Gender: §a" + Utils.capitaliseFirst(listing.getPokemon().getGender().toString()));
		lore.add("§2Shiny: " + (listing.getPokemon().getShiny() ? "§eYes" : "§cNo"));
		lore.add("§2Pokeball: §a" + Utils.capitaliseFirst(listing.getPokemon().getCaughtBall().getName().toString().split(":")[1]));
		lore.add("§7Stats:");
		lore.add("§dHP §8- §3IV: §a" +
				listing.getPokemon().getIvs().get(Stats.HP) + " §cEV: §a" +
				(listing.getPokemon().getEvs().get(Stats.HP) == null ? "0" : listing.getPokemon().getEvs().get(Stats.HP)));
		lore.add("§cAtk §8- §3IV: §a" + listing.getPokemon().getIvs().get(Stats.ATTACK) + " §cEV: §a" +
				(listing.getPokemon().getEvs().get(Stats.ATTACK) == null ? "0" : listing.getPokemon().getEvs().get(Stats.ATTACK)));
		lore.add("§6Def §8- §3IV: §a" + listing.getPokemon().getIvs().get(Stats.DEFENCE) + " §cEV: §a" +
				(listing.getPokemon().getEvs().get(Stats.DEFENCE) == null ? "0" : listing.getPokemon().getEvs().get(Stats.DEFENCE)));
		lore.add("§5SpAtk §8- §3IV: §a" + listing.getPokemon().getIvs().get(Stats.SPECIAL_ATTACK) + " §cEV: §a" +
				(listing.getPokemon().getEvs().get(Stats.SPECIAL_ATTACK) == null ? "0" : listing.getPokemon().getEvs().get(Stats.SPECIAL_ATTACK)));
		lore.add("§eSpDef §8- §3IV: §a" + listing.getPokemon().getIvs().get(Stats.SPECIAL_DEFENCE) + " §cEV: §a" +
				(listing.getPokemon().getEvs().get(Stats.SPECIAL_DEFENCE) == null ? "0" : listing.getPokemon().getEvs().get(Stats.SPECIAL_DEFENCE)));
		lore.add("§3Spe §8- §3IV: §a" + listing.getPokemon().getIvs().get(Stats.SPEED) + " §cEV: §a" +
				(listing.getPokemon().getEvs().get(Stats.SPEED) == null ? "0" : listing.getPokemon().getEvs().get(Stats.SPEED)));
		lore.add("§6Moves:");
		for (Move move : listing.getPokemon().getMoveSet().getMoves()) {
			lore.add("§f" + move.getName());
		}

		return lore;
	}
}
