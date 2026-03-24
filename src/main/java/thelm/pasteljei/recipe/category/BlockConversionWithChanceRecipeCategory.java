package thelm.pasteljei.recipe.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import thelm.jeidrawables.JEIDrawables;
import thelm.pasteljei.recipe.BlockConversionWithChanceRecipe;

/**
 * Based on BlockToBlockWithChanceEmiRecipe
 */
public class BlockConversionWithChanceRecipeCategory extends AbstractUnlockableRecipeCategory<BlockConversionWithChanceRecipe> {

	public final ResourceLocation advancement;

	public BlockConversionWithChanceRecipeCategory(RecipeType<BlockConversionWithChanceRecipe> recipeType, Component title, ResourceLocation advancement) {
		super(recipeType, title);
		this.advancement = advancement;
	}

	@Override
	public int getHeight() {
		return 38;
	}

	@Override
	public boolean isUnlocked(BlockConversionWithChanceRecipe recipe) {
		return hasAdvancement(advancement);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BlockConversionWithChanceRecipe recipe, IFocusGroup focuses) {
		boolean visible = isVisible(recipe);
		addSlot(builder, RecipeIngredientRole.INPUT, 30, 5, JEIDrawables.SLOT, visible).addIngredientsUnsafe(recipe.inputIngredient());
		addSlot(builder, RecipeIngredientRole.OUTPUT, 86, 5, JEIDrawables.OUTPUT_SLOT, visible).addIngredientsUnsafe(recipe.outputIngredient());
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, BlockConversionWithChanceRecipe recipe, IFocusGroup focuses) {
		if(isVisible(recipe)) {
			builder.addDrawable(JEIDrawables.RECIPE_ARROW, 53, 5);
		}
	}

	@Override
	public void draw(BlockConversionWithChanceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipe)) {
			Font font = font();
			Component chanceComponent = Component.translatable("container.pastel.rei.chance", recipe.chance() * 100);
			guiGraphics.drawString(font, chanceComponent, getWidth() / 2 - font.width(chanceComponent) / 2, 29, 0x3F3F3F, false);
		}
	}

	@Override
	public ResourceLocation getRegistryName(BlockConversionWithChanceRecipe recipe) {
		return BuiltInRegistries.BLOCK.getKey(recipe.input().getBlock());
	}
}
