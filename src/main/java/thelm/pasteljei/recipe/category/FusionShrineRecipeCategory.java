package thelm.pasteljei.recipe.category;

import java.util.List;

import earth.terrarium.pastel.api.recipe.IngredientStack;
import earth.terrarium.pastel.recipe.fusion_shrine.FusionShrineRecipe;
import earth.terrarium.pastel.registries.PastelBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.pasteljei.PastelJEI;

/**
 * Based on FusionShrineEmiRecipeGated
 */
public class FusionShrineRecipeCategory extends AbstractGatedRecipeCategory<FusionShrineRecipe> {

	public static final Component TITLE = Component.translatable("block.pastel.fusion_shrine");

	public FusionShrineRecipeCategory() {
		super(PastelJEI.FUSION_SHRINE, TITLE);
	}

	@Override
	public int getHeight() {
		return 80;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<FusionShrineRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		FusionShrineRecipe recipe = recipeHolder.value();
		if(!recipe.getFluid().isEmpty()) {
			addItem(builder, RecipeIngredientRole.CATALYST, 10, 26, PastelBlocks.FUSION_SHRINE_BASALT.toStack(), visible);
			addFluid(builder, RecipeIngredientRole.INPUT, 30, 26, recipe.getFluid(), fluidHelper().bucketVolume(), JEIDrawables.SLOT, visible);
		}
		else {
			addItem(builder, RecipeIngredientRole.CATALYST, 20, 26, PastelBlocks.FUSION_SHRINE_BASALT.toStack(), visible);
		}
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int startX = 1 + getWidth() / 2 - ingredients.size() * 9;
		for(int i = 0; i < ingredients.size(); ++i) {
			addItem(builder, RecipeIngredientRole.INPUT, startX + i * 18, 1, ingredients.get(i).getItems().toList(), JEIDrawables.SLOT, visible);
		}
		addItem(builder, RecipeIngredientRole.OUTPUT, 94, 26, recipe.getResultItem(registryAccess()), JEIDrawables.OUTPUT_SLOT, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<FusionShrineRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			FusionShrineRecipe recipe = recipeHolder.value();
			builder.addDrawable(JEIDrawables.recipeArrow(recipe.getCraftingTime() * 50), 57, 26);
		}
	}

	@Override
	public void draw(RecipeHolder<FusionShrineRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			FusionShrineRecipe recipe = recipeHolder.value();
			Font font = font();
			if(recipe.getDescription().isPresent()) {
				List<FormattedCharSequence> lines = font.split(recipe.getDescription().get(), 136);
				for(int i = 0; i < lines.size(); ++i) {
					guiGraphics.drawString(font, lines.get(i), 0, 50 + i * 10, 0x3F3F3F, false);
				}
			}
			Component timeComponent = getTimeComponent(recipe.getCraftingTime(), recipe.getExperience());
			guiGraphics.drawString(font, timeComponent, getWidth() / 2 - font.width(timeComponent) / 2, 70, 0x3F3F3F, false);
		}
	}
}
