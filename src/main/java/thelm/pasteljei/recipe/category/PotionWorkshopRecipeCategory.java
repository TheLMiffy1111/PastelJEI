package thelm.pasteljei.recipe.category;

import java.util.List;

import earth.terrarium.pastel.PastelCommon;
import earth.terrarium.pastel.api.recipe.IngredientStack;
import earth.terrarium.pastel.recipe.potion_workshop.PotionWorkshopRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.ResourceDrawable;

/**
 * Based on PotionWorkshopEmiRecipeGated
 */
public class PotionWorkshopRecipeCategory<R extends PotionWorkshopRecipe> extends AbstractGatedRecipeCategory<R> {

	public static final ResourceLocation BACKGROUND = PastelCommon.locate("textures/gui/container/potion_workshop_3_slots.png");
	public static final ResourceDrawable BUBBLES = new ResourceDrawable(BACKGROUND, 176, 0, 11, 27);

	public PotionWorkshopRecipeCategory(RecipeType<RecipeHolder<R>> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public int getHeight() {
		return 66;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<R> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		R recipe = recipeHolder.value();
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		addItem(builder, RecipeIngredientRole.INPUT, 31, 49, ingredients.get(0).getItems().toList(), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 78, 5, ingredients.get(1).getItems().toList(), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 31, 1, ingredients.get(2).getItems().toList(), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 13, 25, ingredients.get(3).getItems().toList(), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 49, 25, ingredients.get(4).getItems().toList(), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.OUTPUT, 107, 25, recipe.getResultItem(registryAccess()), JEIDrawables.SLOT, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<R> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			R recipe = recipeHolder.value();
			builder.addDrawable(BUBBLES, 33, 20);
			builder.addDrawable(JEIDrawables.recipeArrow(recipe.getCraftingTime() * 50), 75, 25);
		}
	}

	@Override
	public void draw(RecipeHolder<R> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			R recipe = recipeHolder.value();
			Font font = font();
			Component timeComponent = getTimeComponent(recipe.getCraftingTime());
			guiGraphics.drawString(font, timeComponent, 52, 56, 0x3F3F3F, false);
		}
	}
}
