package thelm.pasteljei.recipe.category;

import earth.terrarium.pastel.PastelCommon;
import earth.terrarium.pastel.recipe.anvil_crushing.AnvilCrushingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.pasteljei.PastelJEI;

/**
 * Based on AnvilCrushingEmiRecipeGated
 */
public class AnvilCrushingRecipeCategory extends AbstractGatedRecipeCategory<AnvilCrushingRecipe> {

	public static final Component TITLE = Component.translatable("container.pastel.rei.anvil_crushing.title");

	public static final Component LOW_FORCE = Component.translatable("container.pastel.rei.anvil_crushing.low_force_required");
	public static final Component MEDIUM_FORCE = Component.translatable("container.pastel.rei.anvil_crushing.medium_force_required");
	public static final Component HIGH_FORCE = Component.translatable("container.pastel.rei.anvil_crushing.high_force_required");

	public static final ResourceLocation BACKGROUND = PastelCommon.locate("textures/gui/container/anvil_crushing.png");
	public static final ResourceDrawable WALL = new ResourceDrawable(BACKGROUND, 0, 0, 16, 48);
	public static final ResourceDrawable FALL = new ResourceDrawable(BACKGROUND, 16, 1, 16, 16);

	public AnvilCrushingRecipeCategory() {
		super(PastelJEI.ANVIL_CRUSHING, TITLE);
	}

	@Override
	public int getHeight() {
		return 59;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<AnvilCrushingRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		AnvilCrushingRecipe recipe = recipeHolder.value();
		addItem(builder, RecipeIngredientRole.INPUT, 32, 30, recipe.getIngredients().get(0), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.CATALYST, 32, 10, new ItemStack(Items.ANVIL), visible);
		addItem(builder, RecipeIngredientRole.OUTPUT, 105, 15, recipe.getResultItem(registryAccess()), JEIDrawables.OUTPUT_SLOT, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<AnvilCrushingRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			builder.addDrawable(WALL, 10, 0);
			builder.addDrawable(FALL, 32, 0);
			builder.addDrawable(JEIDrawables.RECIPE_ARROW, 60, 15);
		}
	}

	@Override
	public void draw(RecipeHolder<AnvilCrushingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			AnvilCrushingRecipe recipe = recipeHolder.value();
			Font font = font();
			Component xpComponent = Component.translatable("container.pastel.rei.anvil_crushing.plus_xp", recipe.getExperience());
			Component forceComponent = getForceComponent(recipe);
			guiGraphics.drawString(font, xpComponent, 126 - font.width(xpComponent), 38, 0x3F3F3F, false);
			guiGraphics.drawString(font, forceComponent, getWidth() / 2 - font.width(forceComponent) / 2, 50, 0x3F3F3F, false);
		}
	}

	public Component getForceComponent(AnvilCrushingRecipe recipe) {
		if(recipe.getCrushedItemsPerPointOfDamage() >= 1F) {
			return LOW_FORCE;
		}
		else if(recipe.getCrushedItemsPerPointOfDamage() >= 0.5F) {
			return MEDIUM_FORCE;
		}
		else {
			return HIGH_FORCE;
		}
	}
}
