package org.pokesplash.gts.UI.module;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Listing.PokemonListing;
import org.pokesplash.gts.util.Utils;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PokemonInfo {
	public static Collection<Component> parse(PokemonListing listing) {
		Collection<Component> lore = new ArrayList<>();
		Style green = Component.empty().getStyle().withColor(TextColor.parseColor("green"));

//		lore.add("§2Nature: §a" + Utils.capitaliseFirst(listing.getListing().getNature().getName().toString().split(":")[1]));
//		if (Utils.isHA(listing.getListing())) {
//			lore.add("§2Ability: §a" + Utils.capitaliseFirst(listing.getListing().getAbility().getName()) + " §b(HA)");
//		} else {
//			lore.add("§2Ability: §a" + Utils.capitaliseFirst(listing.getListing().getAbility().getName()));
//		}


		lore.add(Component.literal("§2Nature: ").
				append(Component.translatable(listing.getListing().getNature().getDisplayName()).setStyle(green)));

		if (Utils.isHA(listing.getListing())) {
			lore.add(Component.literal("§2Ability: ")
					.append(Component.translatable(listing.getListing().getAbility().getDisplayName()).setStyle(green))
					.append(Component.literal(" §b(HA)")));
		} else {
			lore.add(Component.literal("§2Ability: ")
					.append(Component.translatable(listing.getListing().getAbility().getDisplayName()).setStyle(green)));
		}


		lore.add(Component.literal("§2Level: §a" + listing.getListing().getLevel()));
		lore.add(Component.literal("§2Gender: §a" + Utils.capitaliseFirst(listing.getListing().getGender().toString())));
		lore.add(Component.literal("§2Shiny: " + (listing.getListing().getShiny() ? "§eYes" : "§cNo")));


//		lore.add("§2Pokeball: §a" + Utils.capitaliseFirst(listing.getListing().getCaughtBall().getName().toString().split(":")[1]));

		Item ball = listing.getListing().getCaughtBall().item();
		lore.add(Component.literal("§2Ball: ")
				.append(Component.translatable(ball.getName(new ItemStack(ball)).getString()).setStyle(green)));


		lore.add(Component.literal("§7Stats:"));
		lore.add(Component.literal("§dHP §8- §3IV: §a" +
				(listing.getListing().getIvs().get(Stats.HP) == null ? "0" :
						listing.getListing().getIvs().get(Stats.HP))
				+ " §cEV: §a" + (listing.getListing().getEvs().get(Stats.HP) == null ? "0" : listing.getListing().getEvs().get(Stats.HP))));
		lore.add(Component.literal("§cAtk §8- §3IV: §a" + (listing.getListing().getIvs().get(Stats.ATTACK) == null ? "0" :
				+ listing.getListing().getIvs().get(Stats.ATTACK)) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.ATTACK) == null ? "0" : listing.getListing().getEvs().get(Stats.ATTACK))));
		lore.add(Component.literal("§6Def §8- §3IV: §a" + (listing.getListing().getIvs().get(Stats.DEFENCE) == null ? "0" :
				+ listing.getListing().getIvs().get(Stats.DEFENCE)) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.DEFENCE) == null ? "0" :
						listing.getListing().getEvs().get(Stats.DEFENCE))));
		lore.add(Component.literal("§5SpAtk §8- §3IV: §a" + (listing.getListing().getIvs().get(Stats.SPECIAL_ATTACK) == null ?
				"0" :
				+ listing.getListing().getIvs().get(Stats.SPECIAL_ATTACK)) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.SPECIAL_ATTACK) == null ? "0" : listing.getListing().getEvs().get(Stats.SPECIAL_ATTACK))));
		lore.add(Component.literal("§eSpDef §8- §3IV: §a" + (listing.getListing().getIvs().get(Stats.SPECIAL_DEFENCE) == null ?
				"0" :
				+ listing.getListing().getIvs().get(Stats.SPECIAL_DEFENCE)) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.SPECIAL_DEFENCE) == null ? "0" :
						listing.getListing().getEvs().get(Stats.SPECIAL_DEFENCE))));
		lore.add(Component.literal("§3Spe §8- §3IV: §a" + (listing.getListing().getIvs().get(Stats.SPEED) == null ? "0" :
				+ listing.getListing().getIvs().get(Stats.SPEED)) + " §cEV: §a" +
				(listing.getListing().getEvs().get(Stats.SPEED) == null ? "0" :
						listing.getListing().getEvs().get(Stats.SPEED))));
		lore.add(Component.literal("§6Moves:"));
		Style white = Component.empty().getStyle().withColor(TextColor.parseColor("white"));
		for (Move move : listing.getListing().getMoveSet().getMoves()) {
			lore.add(Component.translatable(move.getTemplate().getDisplayName().getString()).setStyle(white));
		}

		return lore;
	}
}
