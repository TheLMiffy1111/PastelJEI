package thelm.pasteljei.recipe.category;

import earth.terrarium.pastel.recipe.fluid_converting.FluidConvertingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;

/**
 * Based on FluidConvertingEmiRecipeGated
 */
public class FluidConvertingRecipeCategory<R extends FluidConvertingRecipe> extends AbstractGatedRecipeCategory<R> {

	public FluidConvertingRecipeCategory(RecipeType<RecipeHolder<R>> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public int getHeight() {
		return 26;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<R> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		R recipe = recipeHolder.value();
		addItem(builder, RecipeIngredientRole.INPUT, 30, 5, recipe.getIngredients().get(0), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.OUTPUT, 86, 5, recipe.getResultItem(registryAccess()), JEIDrawables.OUTPUT_SLOT, visible);
	}
	
	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<R> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			builder.addDrawable(JEIDrawables.RECIPE_ARROW, 53, 5);
		}
	}
}
