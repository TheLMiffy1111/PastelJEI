package thelm.pasteljei.recipe.category;

import java.util.List;

import earth.terrarium.pastel.api.recipe.DescriptiveGatedRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;

/**
 * Based on IngredientInfoRecipeCategory
 */
public class DescriptiveGatedRecipeCategory<R extends DescriptiveGatedRecipe<?>> extends AbstractGatedRecipeCategory<R> {

	public DescriptiveGatedRecipeCategory(RecipeType<RecipeHolder<R>> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public int getHeight() {
		return 125;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<R> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		R recipe = recipeHolder.value();
		ItemStack stack = new ItemStack(recipe.getItem());
		addItem(builder, RecipeIngredientRole.INPUT, 60, 1, stack, JEIDrawables.SLOT, visible);
		builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(stack);
	}

	@Override
	public void draw(RecipeHolder<R> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			R recipe = recipeHolder.value();
			Font font = font();
			List<FormattedCharSequence> lines = font.split(recipe.getDescription(), getWidth());
			for(int i = 0; i < lines.size(); ++i) {
				guiGraphics.drawString(font, lines.get(i), 0, 22 + i * 10, 0x3F3F3F, false);
			}
		}
	}
}
