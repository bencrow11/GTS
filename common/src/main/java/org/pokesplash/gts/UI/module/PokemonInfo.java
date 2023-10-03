package org.pokesplash.gts.UI.module;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PokemonInfo {
	public static Collection<String> parse(PokemonListing listing) {
		Collection<String> lore = new ArrayList<>();

		lore.add("§2Nature: §a" + Utils.capitaliseFirst(listing.getListing().getNature().getName().toString().split(":")[1]));

		if (Utils.isHA(listing.getListing())) {
			lore.add("§2Ability: §a" + Utils.capitaliseFirst(listing.getListing().getAbility().getName()) + " §b(HA)");
		} else {
			lore.add("§2Ability: §a" + Utils.capitaliseFirst(listing.getListing().getAbility().getName()));
		}

		lore.add("§2Level: §a" + listing.getListing().getLevel());
		lore.add("§2Gender: §a" + Utils.capitaliseFirst(listing.getListing().getGender().toString()));
		lore.add("§2Shiny: " + (listing.getListing().getShiny() ? "§eYes" : "§cNo"));
		lore.add("§2Pokeball: §a" + Utils.capitaliseFirst(listing.getListing().getCaughtBall().getName().toString().split(":")[1]));
		lore.add("§7Stats:");
		lore.add("§dHP §8- §3IV: §a" +
				listing.getListing().getIvs().get(Stats.HP) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.HP) == null ? "0" : listing.getListing().getEvs().get(Stats.HP)));
		lore.add("§cAtk §8- §3IV: §a" + listing.getListing().getIvs().get(Stats.ATTACK) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.ATTACK) == null ? "0" : listing.getListing().getEvs().get(Stats.ATTACK)));
		lore.add("§6Def §8- §3IV: §a" + listing.getListing().getIvs().get(Stats.DEFENCE) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.DEFENCE) == null ? "0" : listing.getListing().getEvs().get(Stats.DEFENCE)));
		lore.add("§5SpAtk §8- §3IV: §a" + listing.getListing().getIvs().get(Stats.SPECIAL_ATTACK) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.SPECIAL_ATTACK) == null ? "0" : listing.getListing().getEvs().get(Stats.SPECIAL_ATTACK)));
		lore.add("§eSpDef §8- §3IV: §a" + listing.getListing().getIvs().get(Stats.SPECIAL_DEFENCE) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.SPECIAL_DEFENCE) == null ? "0" : listing.getListing().getEvs().get(Stats.SPECIAL_DEFENCE)));
		lore.add("§3Spe §8- §3IV: §a" + listing.getListing().getIvs().get(Stats.SPEED) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.SPEED) == null ? "0" : listing.getListing().getEvs().get(Stats.SPEED)));
		lore.add("§6Moves:");
		for (Move move : listing.getListing().getMoveSet().getMoves()) {
			lore.add("§f" + move.getName());
		}

		return lore;
	}
}
