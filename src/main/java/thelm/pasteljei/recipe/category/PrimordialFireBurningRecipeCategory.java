package thelm.pasteljei.recipe.category;

import earth.terrarium.pastel.PastelCommon;
import earth.terrarium.pastel.recipe.primordial_fire_burning.PrimordialFireBurningRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.SpriteDrawable;
import thelm.pasteljei.PastelJEI;

public class PrimordialFireBurningRecipeCategory extends AbstractGatedRecipeCategory<PrimordialFireBurningRecipe> {

	public static final Component TITLE = Component.translatable("container.pastel.rei.primordial_fire_burning.title");

	public static final ResourceLocation FIRE_TEXTURE = PastelCommon.locate("block/primordial_fire_0");
	public static final SpriteDrawable FIRE = new SpriteDrawable(() -> {
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(FIRE_TEXTURE);
	}, 16, 16);

	public PrimordialFireBurningRecipeCategory() {
		super(PastelJEI.PRIMORDIAL_FIRE_BURNING, TITLE);
	}

	@Override
	public int getHeight() {
		return 35;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PrimordialFireBurningRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		PrimordialFireBurningRecipe recipe = recipeHolder.value();
		addItem(builder, RecipeIngredientRole.INPUT, 29, 1, recipe.getIngredients().get(0), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.OUTPUT, 87, 9, recipe.getResultItem(registryAccess()), JEIDrawables.OUTPUT_SLOT, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<PrimordialFireBurningRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			builder.addDrawable(FIRE, 29, 19);
			builder.addDrawable(JEIDrawables.RECIPE_ARROW, 53, 9);
		}
	}
}
