package thelm.pasteljei.recipe.category;

import java.util.List;

import earth.terrarium.pastel.PastelCommon;
import earth.terrarium.pastel.items.magic_items.KnowledgeGemItem;
import earth.terrarium.pastel.recipe.enchanter.EnchanterCraftingRecipe;
import earth.terrarium.pastel.registries.PastelBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.pasteljei.PastelJEI;

/**
 * Based on EnchanterEmiRecipeGated
 */
public class EnchanterRecipeCategory extends AbstractGatedRecipeCategory<EnchanterCraftingRecipe> {

	public static final Component TITLE = Component.translatable("container.pastel.rei.enchanting.title");

	public static final ResourceLocation BACKGROUND = PastelCommon.locate("textures/gui/container/enchanter.png");
	public static final ResourceDrawable ALTAR = new ResourceDrawable(BACKGROUND, 0, 0, 54, 54);

	public EnchanterRecipeCategory() {
		super(PastelJEI.ENCHANTER, TITLE);
	}

	@Override
	public int getHeight() {
		return 80;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<EnchanterCraftingRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		EnchanterCraftingRecipe recipe = recipeHolder.value();
		List<Ingredient> ingredients = recipe.getIngredients();
		addItem(builder, RecipeIngredientRole.INPUT, 113, 7, KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredExperience(), true), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.CATALYST, 113, 53, PastelBlocks.ENCHANTER.toStack(), visible);
		addItem(builder, RecipeIngredientRole.INPUT, 34, 32, ingredients.get(0), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 21, 1, ingredients.get(1), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 47, 1, ingredients.get(2), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 65, 19, ingredients.get(3), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 65, 45, ingredients.get(4), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 47, 63, ingredients.get(5), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 21, 63, ingredients.get(6), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 3, 45, ingredients.get(7), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 3, 19, ingredients.get(8), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.OUTPUT, 113, 32, recipe.getResultItem(registryAccess()), JEIDrawables.OUTPUT_SLOT, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<EnchanterCraftingRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			EnchanterCraftingRecipe recipe = recipeHolder.value();
			builder.addDrawable(JEIDrawables.recipeArrow(recipe.getCraftingTime(1) * 50), 84, 32);
		}
	}

	@Override
	public void draw(RecipeHolder<EnchanterCraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			EnchanterCraftingRecipe recipe = recipeHolder.value();
			ALTAR.draw(guiGraphics, 15, 13);
			Font font = font();
			Component timeComponent = getTimeComponent(recipe.getCraftingTime(1));
			guiGraphics.drawString(font, timeComponent, 69, 71, 0x3F3F3F, false);
		}
	}
}
