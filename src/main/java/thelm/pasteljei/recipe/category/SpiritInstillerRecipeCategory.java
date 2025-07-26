package thelm.pasteljei.recipe.category;

import java.util.List;

import earth.terrarium.pastel.api.recipe.IngredientStack;
import earth.terrarium.pastel.helpers.render.LoreHelper;
import earth.terrarium.pastel.recipe.spirit_instiller.SpiritInstillerRecipe;
import earth.terrarium.pastel.recipe.spirit_instiller.dynamic.spawner_manipulation.SpawnerChangeRecipe;
import earth.terrarium.pastel.registries.PastelBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.pasteljei.PastelJEI;

/**
 * Based on SpiritInstillingEmiRecipeGated
 */
public class SpiritInstillerRecipeCategory extends AbstractGatedRecipeCategory<SpiritInstillerRecipe> {

	public static final Component TITLE = Component.translatable("block.pastel.spirit_instiller");

	public SpiritInstillerRecipeCategory() {
		super(PastelJEI.SPIRIT_INSTILLER, TITLE);
	}

	@Override
	public int getHeight() {
		return 48;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<SpiritInstillerRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		SpiritInstillerRecipe recipe = recipeHolder.value();
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		addItem(builder, RecipeIngredientRole.INPUT, 31, 1, ingredients.get(0).getItems().toList(), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 11, 1, ingredients.get(1).getItems().toList(), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 51, 1, ingredients.get(2).getItems().toList(), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.CATALYST, 31, 18, PastelBlocks.SPIRIT_INSTILLER.toStack(), visible);
		addItem(builder, RecipeIngredientRole.CATALYST, 11, 18, PastelBlocks.ITEM_BOWL_CALCITE.toStack(), visible);
		addItem(builder, RecipeIngredientRole.CATALYST, 51, 18, PastelBlocks.ITEM_BOWL_CALCITE.toStack(), visible);
		ItemStack stack = recipe.getResultItem(registryAccess());
		if(recipe instanceof SpawnerChangeRecipe spawnerChange) {
			LoreHelper.setLore(stack, spawnerChange.getOutputLoreText());
		}
		addItem(builder, RecipeIngredientRole.OUTPUT, 105, 10, stack, JEIDrawables.OUTPUT_SLOT, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<SpiritInstillerRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			SpiritInstillerRecipe recipe = recipeHolder.value();
			builder.addDrawable(JEIDrawables.recipeArrow(recipe.getCraftingTime() * 50), 73, 10);
		}
	}

	@Override
	public void draw(RecipeHolder<SpiritInstillerRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			SpiritInstillerRecipe recipe = recipeHolder.value();
			Font font = font();
			Component timeComponent = getTimeComponent(recipe.getCraftingTime(), recipe.getExperience());
			guiGraphics.drawString(font, timeComponent, getWidth() / 2 - font.width(timeComponent) / 2, 38, 0x3F3F3F, false);
		}
	}
}
