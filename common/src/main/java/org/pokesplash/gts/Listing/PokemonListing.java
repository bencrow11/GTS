package org.pokesplash.gts.Listing;

import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import org.pokesplash.gts.Gts;

import java.util.UUID;

/**
 * Class that holds a single listing.
 */
public class PokemonListing extends Listing<Pokemon> {
	// The Pokemon that is being listed.
	private final JsonElement pokemon;

	/**
	 * Constructor to create a new listing.
	 * @param sellerUuid The UUID of the person selling the Pokemon.
	 * @param sellerName The name of the seller.
	 * @param price The price the Pokemon is selling for.
	 * @param pokemon The Pokemon for sale.
	 */
	public PokemonListing(UUID sellerUuid, String sellerName, double price, Pokemon pokemon) {
		super(sellerUuid, sellerName, price, true);
		this.pokemon = Pokemon.getCODEC().encodeStart(JsonOps.INSTANCE, pokemon).getOrThrow();
	}

	public PokemonListing(PokemonListing other) {
		super(UUID.fromString(other.getSellerUuid().toString()),
				String.copyValueOf(other.getSellerName().toCharArray()),
				other.getPrice(), true);
		this.pokemon = Pokemon.getCODEC().encodeStart(JsonOps.INSTANCE, other.getListing()).getOrThrow();
		super.id = UUID.fromString(other.getId().toString());
		super.version = String.copyValueOf(other.getVersion().toCharArray());
		super.setEndTime(other.getEndTime());
	}

	@Override
	public Pokemon getListing() {
		return Pokemon.getCODEC().decode(JsonOps.INSTANCE, pokemon).getOrThrow().getFirst();
	}

	@Override
	public boolean isListingValid() {
		return pokemon != null && pokemon.isJsonObject();
	}

	public JsonElement getListingAsJsonObject() {
		return pokemon;
	}

	@Override
	public MutableComponent getDisplayName() {
		Style base = Style.EMPTY.withItalic(false);
		Style blue = base.withColor(TextColor.parseColor("blue").getOrThrow());
		Style dark_aqua = base.withColor(TextColor.parseColor("dark_aqua").getOrThrow());
		Style red = base.withColor(TextColor.parseColor("red").getOrThrow());
		Style yellow = base.withColor(TextColor.parseColor("yellow").getOrThrow());
		Style white = base.withColor(TextColor.parseColor("white").getOrThrow());
		Pokemon pokemon = this.getListing();
		boolean isShiny = pokemon.getShiny();
		MutableComponent displayName = pokemon.getDisplayName().setStyle(isShiny ? yellow : dark_aqua);
		if (isShiny) {
			displayName.append(Component.literal("★").setStyle(red));
		}
		displayName.append(" ").append(Component.translatable("cobblemon.ui.lv.number", pokemon.getLevel()).setStyle(white));
		switch (pokemon.getGender().toString()) {
			case "MALE":
				displayName.append(Component.literal(" ♂").setStyle(blue));
				break;
			case "FEMALE":
				displayName.append(Component.literal(" ♀").setStyle(red));
				break;
			default:
				break;
		}
		return displayName;
	}

	@Override
	public String getUiTitle() {
		return Gts.language.getPokemonTitle();
	}

	@Override
	public ItemStack getIcon() {
		return PokemonItem.from(getListing(), 1);
	}

	@Override
	public Listing deepClone() {
		return new PokemonListing(this);
	}

	@Override
	public String getListingName() {
		return getListing().getSpecies().toString();
	}
}
