package thelm.pasteljei.recipe.transfer;

import earth.terrarium.pastel.inventories.PastelScreenHandlerTypes;
import earth.terrarium.pastel.inventories.PedestalScreenHandler;
import earth.terrarium.pastel.recipe.pedestal.PedestalRecipe;
import earth.terrarium.pastel.recipe.pedestal.PedestalRecipeTier;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.pasteljei.PastelJEI;

public class PedestalRecipeTransferInfo extends GatedRecipeTransferInfo<PedestalScreenHandler, PedestalRecipe> {

	public final PedestalRecipeTier tier;

	public PedestalRecipeTransferInfo(PedestalRecipeTier tier) {
		super(PedestalScreenHandler.class, PastelScreenHandlerTypes.PEDESTAL, getRecipeType(tier), 0, getRecipeSlotCount(tier), 16, 36);
		this.tier = tier;
	}

	public static RecipeType<RecipeHolder<PedestalRecipe>> getRecipeType(PedestalRecipeTier tier) {
		return switch(tier) {
		case BASIC -> PastelJEI.PEDESTAL_BASIC;
		case SIMPLE -> PastelJEI.PEDESTAL_SIMPLE;
		case ADVANCED -> PastelJEI.PEDESTAL_ADVANCED;
		case COMPLEX -> PastelJEI.PEDESTAL_COMPLEX;
		};
	}

	public static int getRecipeSlotCount(PedestalRecipeTier tier) {
		return switch(tier) {
		case BASIC, SIMPLE -> 12;
		case ADVANCED -> 13;
		case COMPLEX -> 14;
		};
	}

	@Override
	public boolean canHandle(PedestalScreenHandler container, RecipeHolder<PedestalRecipe> recipeHolder) {
		return super.canHandle(container, recipeHolder) && container.getPedestalRecipeTier().compareTo(tier) >= 0;
	}
}
