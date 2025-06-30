package thelm.pasteljei.recipe.category;

import java.util.List;

import earth.terrarium.pastel.PastelCommon;
import earth.terrarium.pastel.recipe.crystallarieum.CrystallarieumCatalyst;
import earth.terrarium.pastel.recipe.crystallarieum.CrystallarieumRecipe;
import earth.terrarium.pastel.registries.PastelBlocks;
import earth.terrarium.pastel.registries.PastelDataComponentTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.pasteljei.PastelJEI;

/**
 * Based on CrystallarieumEmiRecipeGated
 */
public class CrystallarieumRecipeCategory extends AbstractGatedRecipeCategory<CrystallarieumRecipe> {

	public static final Component TITLE = Component.translatable("block.pastel.crystallarieum");

	public static final Component CATALYST = Component.translatable("container.pastel.rei.crystallarieum.catalyst");
	public static final Component ACCELERATOR = Component.translatable("container.pastel.rei.crystallarieum.accelerator");
	public static final Component INK_CONSUMPTION = Component.translatable("container.pastel.rei.crystallarieum.ink_consumption");
	public static final Component USED_UP = Component.translatable("container.pastel.rei.crystallarieum.used_up");

	public static final ResourceLocation BACKGROUND = PastelCommon.locate("textures/gui/modonomicon/crystallarieum.png");
	public static final ResourceDrawable ACCEL_HIGHER = new ResourceDrawable(BACKGROUND, 98, 0, 7, 7, 128, 128);
	public static final ResourceDrawable ACCEL_HIGH = new ResourceDrawable(BACKGROUND, 91, 0, 7, 7, 128, 128);
	public static final ResourceDrawable ACCEL_NONE = new ResourceDrawable(BACKGROUND, 84, 0, 7, 7, 128, 128);
	public static final ResourceDrawable ACCEL_LOW = new ResourceDrawable(BACKGROUND, 77, 0, 7, 7, 128, 128);
	public static final ResourceDrawable ACCEL_LOWER = new ResourceDrawable(BACKGROUND, 70, 0, 7, 7, 128, 128);
	public static final ResourceDrawable CONSUME_HIGHER = new ResourceDrawable(BACKGROUND, 70, 7, 7, 7, 128, 128);
	public static final ResourceDrawable CONSUME_HIGH = new ResourceDrawable(BACKGROUND, 77, 7, 7, 7, 128, 128);
	public static final ResourceDrawable CONSUME_NORMAL = new ResourceDrawable(BACKGROUND, 84, 7, 7, 7, 128, 128);
	public static final ResourceDrawable CONSUME_LOW = new ResourceDrawable(BACKGROUND, 91, 7, 7, 7, 128, 128);
	public static final ResourceDrawable CONSUME_LOWER = new ResourceDrawable(BACKGROUND, 98, 7, 7, 7, 128, 128);
	public static final ResourceDrawable CHANCE_HIGHER = new ResourceDrawable(BACKGROUND, 70, 14, 7, 7, 128, 128);
	public static final ResourceDrawable CHANCE_HIGH = new ResourceDrawable(BACKGROUND, 77, 14, 7, 7, 128, 128);
	public static final ResourceDrawable CHANCE_NORMAL = new ResourceDrawable(BACKGROUND, 84, 14, 7, 7, 128, 128);
	public static final ResourceDrawable CHANCE_LOW = new ResourceDrawable(BACKGROUND, 91, 14, 7, 7, 128, 128);
	public static final ResourceDrawable CHANCE_NONE = new ResourceDrawable(BACKGROUND, 98, 14, 7, 7, 128, 128);

	public CrystallarieumRecipeCategory() {
		super(PastelJEI.CRYSTALLARIEUM, TITLE);
	}

	@Override
	public int getHeight() {
		return 100;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CrystallarieumRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		CrystallarieumRecipe recipe = recipeHolder.value();
		addItem(builder, RecipeIngredientRole.INPUT, 7, 9, recipe.getIngredientStack(), JEIDrawables.SLOT, visible);
		ItemStack stack = PastelBlocks.CRYSTALLARIEUM.toStack();
		stack.set(PastelDataComponentTypes.INK_COLOR, recipe.getInkColor());
		addItem(builder, RecipeIngredientRole.CATALYST, 27, 19, stack, visible);
		List<ItemStack> growthStages = recipe.getGrowthStages().stream().map(BlockState::getBlock).map(ItemStack::new).toList();
		addItem(builder, RecipeIngredientRole.INPUT, 27, 1, growthStages.get(0), JEIDrawables.SLOT, visible);
		for(int i = 1; i < growthStages.size(); ++i) {
			addItem(builder, RecipeIngredientRole.OUTPUT, 53 + i * 20, 9, growthStages.get(i), JEIDrawables.SLOT, visible);
		}
		List<CrystallarieumCatalyst> catalysts = recipe.getCatalysts();
		for(int i = 0; i < catalysts.size(); ++i) {
			int x = 53 + i * 18;
			addItem(builder, RecipeIngredientRole.CATALYST, x, 39, catalysts.get(i).ingredient(), JEIDrawables.SLOT, visible);
		}
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<CrystallarieumRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			CrystallarieumRecipe recipe = recipeHolder.value();
			builder.addDrawable(JEIDrawables.recipeArrow(recipe.getSecondsPerGrowthStage() * 1000), 47, 9);
			List<CrystallarieumCatalyst> catalysts = recipe.getCatalysts();
			for(int i = 0; i < catalysts.size(); ++i) {
				CrystallarieumCatalyst catalyst = catalysts.get(i);
				int x = 58 + i * 18;
				IDrawable icon;

				float growthAcceleration = catalyst.growthAccelerationMod();
				if(growthAcceleration >= 5F) {
					icon = ACCEL_HIGHER;
				}
				else if(growthAcceleration > 1F) {
					icon = ACCEL_HIGH;
				}
				else if(growthAcceleration == 1F) {
					icon = ACCEL_NONE;
				}
				else if(growthAcceleration >= 0.2F) {
					icon = ACCEL_LOW;
				}
				else {
					icon = ACCEL_LOWER;
				}
				builder.addDrawable(icon, x, 59);

				float inkConsumption = catalyst.inkConsumptionMod();
				if(inkConsumption >= 5F) {
					icon = CONSUME_HIGHER;
				}
				else if(inkConsumption > 1F) {
					icon = CONSUME_HIGH;
				}
				else if(inkConsumption == 1F) {
					icon = CONSUME_NORMAL;
				}
				else if(inkConsumption >= 0.2F) {
					icon = CONSUME_LOW;
				}
				else {
					icon = CONSUME_LOWER;
				}
				builder.addDrawable(icon, x, 69);

				float consumeChance = catalyst.consumeChancePerSecond();
				if(consumeChance >= 0.25F) {
					icon = CHANCE_HIGHER;
				}
				else if(consumeChance >= 0.05F) {
					icon = CHANCE_HIGH;
				}
				else if(consumeChance >= 0.02F) {
					icon = CHANCE_NORMAL;
				}
				else if(consumeChance > 1e-4F) {
					icon = CHANCE_LOW;
				}
				else {
					icon = CHANCE_NONE;
				}
				builder.addDrawable(icon, x, 79);
			}
		}
	}

	@Override
	public void draw(RecipeHolder<CrystallarieumRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			CrystallarieumRecipe recipe = recipeHolder.value();
			Font font = font();
			Component timeComponent;
			if(recipe.growsWithoutCatalyst()) {
				timeComponent = Component.translatable("container.pastel.rei.crystallarieum.crafting_time_per_stage_seconds_catalyst_optional", recipe.getSecondsPerGrowthStage());
			}
			else {
				timeComponent = Component.translatable("container.pastel.rei.crystallarieum.crafting_time_per_stage_seconds", recipe.getSecondsPerGrowthStage());
			}
			guiGraphics.drawString(font, CATALYST, 6, 43, 0x3F3F3F, false);
			guiGraphics.drawString(font, ACCELERATOR, 6, 58, 0x3F3F3F, false);
			guiGraphics.drawString(font, INK_CONSUMPTION, 6, 68, 0x3F3F3F, false);
			guiGraphics.drawString(font, USED_UP, 6, 78, 0x3F3F3F, false);
			guiGraphics.drawString(font, timeComponent, getWidth() / 2 - font.width(timeComponent) / 2, 90, 0x3F3F3F, false);
		}
	}
}
