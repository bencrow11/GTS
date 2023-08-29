package org.pokesplash.gts.UI;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.Template;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.CobblemonItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * UI of the Pokemon Listings page.
 */
public class PokemonListings {

	// The title of the page.
	private String title;

	/**
	 * Constructor that sets the title of the page.
	 * @param title
	 */
	public PokemonListings(String title) {
		this.title = title;
	}

	/**
	 * Method that returns the page.
	 * @return Pokemon Listings page.
	 */
	public Page getPage() {
		Button sortByPriceButton = GooeyButton.builder()
				.display(new ItemStack(Items.GOLD_NUGGET))
				.title("§eSort By Price")
				.onClick((action) -> {
					System.out.println("Sort items by price");
				})
				.build();

		Button sortByNewestButton = GooeyButton.builder()
				.display(new ItemStack(Items.CLOCK))
				.title("§3Sort By Newest")
				.onClick((action) -> {
					System.out.println("Sort items by newest");
				})
				.build();

		Button sortByNameButton = GooeyButton.builder()
				.display(new ItemStack(Items.OAK_SIGN))
				.title("§6Sort By Name")
				.onClick((action) -> {
					System.out.println("Sort items by name");
				})
				.build();

		Button seeItemListings = GooeyButton.builder()
				.display(new ItemStack(CobblemonItems.ASSAULT_VEST.get()))
				.title("§9See Item Listings")
				.onClick((action) -> {
					System.out.println("See item listings");
				})
				.build();

		Button manageListings = GooeyButton.builder()
				.display(new ItemStack(CobblemonItems.SACHET.get()))
				.title("§dManage Listings")
				.onClick((action) -> {
					System.out.println("Manage listings");
				})
				.build();

		Button nextPage = GooeyButton.builder()
				.display(new ItemStack(Items.ARROW))
				.title("§7Next Page")
				.onClick((action) -> {
					System.out.println("Next Page");
				})
				.build();

		Button previousPage = GooeyButton.builder()
				.display(new ItemStack(CobblemonItems.POISON_BARB.get()))
				.title("§7Previous Page")
				.onClick((action) -> {
					System.out.println("Previous Page");
				})
				.build();

		Template template = ChestTemplate.builder(6)
				.set(47, sortByPriceButton)
				.set(48, sortByNewestButton)
				.set(49, sortByNameButton)
				.set(50, seeItemListings)
				.set(51, manageListings)
				.set(53, nextPage)
				.set(45, previousPage)
				.build();

		return GooeyPage.builder()
				.template(template)
				.title(title)
				.build();
	}
}
