package thelm.pasteljei.recipe.category;

import java.util.List;

import earth.terrarium.pastel.api.recipe.IngredientStack;
import earth.terrarium.pastel.recipe.titration_barrel.ITitrationBarrelRecipe;
import earth.terrarium.pastel.recipe.titration_barrel.TitrationBarrelRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.pasteljei.PastelJEI;

/**
 * Based on TitrationBarrelEmiRecipeGated
 */
public class TitrationBarrelRecipeCategory extends AbstractGatedRecipeCategory<ITitrationBarrelRecipe> {

	public static final Component TITLE = Component.translatable("block.pastel.titration_barrel");

	public TitrationBarrelRecipeCategory() {
		super(PastelJEI.TITRATION_BARREL, TITLE);
	}

	@Override
	public int getHeight() {
		return 50;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ITitrationBarrelRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		ITitrationBarrelRecipe recipe = recipeHolder.value();
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		int inputCount = ingredients.size();
		boolean hasFluid = false;
		if(!recipe.getFluidInput().isEmpty()) {
			inputCount++;
			hasFluid = true;
		}
		int startX = Math.max(11, 41 - inputCount * 10);
		int startY = inputCount > 3 ? 1 : 11;
		if(hasFluid) {
			addFluid(builder, RecipeIngredientRole.INPUT, startX, startY, recipe.getFluidInput(), fluidHelper().bucketVolume(), JEIDrawables.SLOT, visible);
		}
		for(int i = 0; i < ingredients.size(); ++i) {
			int x = startX + (hasFluid ? i + 1 : i) % 3 * 20;
			int y = startY + (hasFluid ? i + 1 : i) / 3 * 20;
			addItem(builder, RecipeIngredientRole.INPUT, x, y, ingredients.get(i).getItems().toList(), JEIDrawables.SLOT, visible);
		}
		if(recipe.getTappingItem() != null && recipe.getTappingItem() != Items.AIR) {
			addItem(builder, RecipeIngredientRole.INPUT, 76, 21, new ItemStack(recipe.getTappingItem()), JEIDrawables.SLOT, visible);
		}
		List<ItemStack> outputVariations;
		if(recipe instanceof TitrationBarrelRecipe titrationBarrelRecipe && titrationBarrelRecipe.getFermentationData() != null) {
			outputVariations = List.copyOf(titrationBarrelRecipe.getOutputVariations(TitrationBarrelRecipe.FERMENTATION_DURATION_DISPLAY_TIME_MULTIPLIERS));
		}
		else {
			outputVariations = List.of(recipe.getResultItem(registryAccess()));
		}
		addItem(builder, RecipeIngredientRole.OUTPUT, 105, 10, outputVariations, JEIDrawables.OUTPUT_SLOT, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<ITitrationBarrelRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			ITitrationBarrelRecipe recipe = recipeHolder.value();
			IDrawable recipeArrow = JEIDrawables.recipeArrow(recipe.getMinFermentationTimeHours() * 1000);
			if(recipe.getTappingItem() == null || recipe.getTappingItem() == Items.AIR) {
				builder.addDrawable(recipeArrow, 73, 10);
			}
			else {
				builder.addDrawable(recipeArrow, 73, 2);
			}
		}
	}

	@Override
	public void draw(RecipeHolder<ITitrationBarrelRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			ITitrationBarrelRecipe recipe = recipeHolder.value();
			Font font = font();
			Component durationComponent = TitrationBarrelRecipe.getDurationText(recipe.getMinFermentationTimeHours(), recipe.getFermentationData());
			guiGraphics.drawString(font, durationComponent, getWidth() / 2 - font.width(durationComponent) / 2, 40, 0x3F3F3F, false);
		}
	}

}
