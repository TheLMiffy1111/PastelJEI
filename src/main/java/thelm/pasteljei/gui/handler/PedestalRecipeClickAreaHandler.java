package thelm.pasteljei.gui.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import earth.terrarium.pastel.PastelCommon;
import earth.terrarium.pastel.inventories.PedestalScreen;
import earth.terrarium.pastel.recipe.pedestal.PedestalTier;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.RecipeType;
import thelm.pasteljei.PastelJEI;

public class PedestalRecipeClickAreaHandler implements IGuiContainerHandler<PedestalScreen> {

	@Override
	public Collection<IGuiClickableArea> getGuiClickableAreas(PedestalScreen containerScreen, double guiMouseX, double guiMouseY) {
		PedestalTier tier = containerScreen.getMenu().getTier();
		List<RecipeType<?>> recipeTypes = new ArrayList<>();
		recipeTypes.add(PastelJEI.PEDESTAL_BASIC);
		if(tier.ordinal() > 0) {
			recipeTypes.add(PastelJEI.PEDESTAL_SIMPLE);
		}
		if(tier.ordinal() > 1) {
			recipeTypes.add(PastelJEI.PEDESTAL_ADVANCED);
		}
		if(tier.ordinal() > 2) {
			recipeTypes.add(PastelJEI.PEDESTAL_COMPLEX);
		}
		if(PastelCommon.CONFIG.canPedestalCraftVanillaRecipes()) {
			recipeTypes.add(RecipeTypes.CRAFTING);
		}
		return List.of(IGuiClickableArea.createBasic(89, 37, 22, 16, recipeTypes.toArray(RecipeType<?>[]::new)));
	}
}
