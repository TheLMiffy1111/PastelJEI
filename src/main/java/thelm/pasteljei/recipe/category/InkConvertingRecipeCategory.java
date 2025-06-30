package thelm.pasteljei.recipe.category;

import earth.terrarium.pastel.recipe.InkConvertingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.pasteljei.PastelJEI;

/**
 * Based on InkConvertingEmiRecipeGated
 */
public class InkConvertingRecipeCategory extends AbstractGatedRecipeCategory<InkConvertingRecipe> {

	public static final Component TITLE = Component.translatable("container.pastel.rei.ink_converting.title");

	public InkConvertingRecipeCategory() {
		super(PastelJEI.INK_CONVERTING, TITLE);
	}

	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<InkConvertingRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		InkConvertingRecipe recipe = recipeHolder.value();
		addItem(builder, RecipeIngredientRole.INPUT, 1, 2, recipe.getIngredients().get(0), JEIDrawables.SLOT, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<InkConvertingRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			builder.addDrawable(JEIDrawables.RECIPE_ARROW, 22, 2);
		}
	}

	@Override
	public void draw(RecipeHolder<InkConvertingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			InkConvertingRecipe recipe = recipeHolder.value();
			Font font = font();
			Component colorComponent = Component.translatable("container.pastel.rei.ink_converting.color", recipe.getInkColor().getName());
			Component amountComponent = Component.translatable("container.pastel.rei.ink_converting.amount", recipe.getInkAmount());
			guiGraphics.drawString(font, colorComponent, 50, 1, 0x3F3F3F, false);
			guiGraphics.drawString(font, amountComponent, 50, 11, 0x3F3F3F, false);
		}
	}
}
