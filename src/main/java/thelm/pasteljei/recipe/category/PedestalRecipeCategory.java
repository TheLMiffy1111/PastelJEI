package thelm.pasteljei.recipe.category;

import java.util.ArrayList;
import java.util.List;

import earth.terrarium.pastel.api.item.GemstoneColor;
import earth.terrarium.pastel.api.recipe.IngredientStack;
import earth.terrarium.pastel.inventories.PedestalScreen;
import earth.terrarium.pastel.recipe.pedestal.PastelGemstoneColor;
import earth.terrarium.pastel.recipe.pedestal.PedestalRecipe;
import earth.terrarium.pastel.recipe.pedestal.PedestalTier;
import earth.terrarium.pastel.registries.PastelBlocks;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.CyclingDrawable;
import thelm.jeidrawables.gui.render.IngredientDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.pasteljei.PastelJEI;

/**
 * Based on PedestalCraftingEmiRecipeGated
 */
public class PedestalRecipeCategory extends AbstractGatedRecipeCategory<PedestalRecipe> {

	public static final Component TITLE_BASIC = Component.translatable("block.pastel.pedestal");
	public static final Component TITLE_SIMPLE = Component.translatable("multiblock.pastel.pedestal_simple");
	public static final Component TITLE_ADVANCED = Component.translatable("multiblock.pastel.pedestal_advanced");
	public static final Component TITLE_COMPLEX = Component.translatable("multiblock.pastel.pedestal_complex");

	public static final IDrawable BASIC_ICON = new CyclingDrawable(1000,
			new IngredientDrawable<>(PastelBlocks.PEDESTAL_BASIC_TOPAZ.toStack()),
			new IngredientDrawable<>(PastelBlocks.PEDESTAL_BASIC_AMETHYST.toStack()),
			new IngredientDrawable<>(PastelBlocks.PEDESTAL_BASIC_CITRINE.toStack()));

	public final PedestalTier tier;
	public final int powderSlotCount;

	public final ResourceLocation background;
	public final ResourceDrawable[] inputSlots;
	public final ResourceDrawable[] powderSlots;
	public final ResourceDrawable outputSlot;
	public final ResourceDrawable tierOverlay;

	public PedestalRecipeCategory(PedestalTier tier) {
		super(getRecipeType(tier), getTitle(tier));
		this.tier = tier;
		powderSlotCount = tier.getPowderSlotCount();

		background = PedestalScreen.getBackgroundTextureForTier(tier);
		inputSlots = new ResourceDrawable[9];
		for(int y = 0; y < 3; ++y) {
			for(int x = 0; x < 3; ++x) {
				inputSlots[y * 3 + x] = new ResourceDrawable(background, 29 + x * 18, 18 + y * 18, 18, 18);
			}
		}
		int powderSlotU = 88 - powderSlotCount * 9;
		powderSlots = new ResourceDrawable[powderSlotCount];
		for(int i = 0; i < powderSlotCount; ++i) {
			powderSlots[i] = new ResourceDrawable(background, powderSlotU + i * 18, 76, 18, 18);
		}
		outputSlot = new ResourceDrawable(background, 122, 32, 26, 26);
		tierOverlay = new ResourceDrawable(background, 200, 0, 40, 16);
	}

	public static RecipeType<RecipeHolder<PedestalRecipe>> getRecipeType(PedestalTier tier) {
		return switch(tier) {
		case BASIC -> PastelJEI.PEDESTAL_BASIC;
		case SIMPLE -> PastelJEI.PEDESTAL_SIMPLE;
		case ADVANCED -> PastelJEI.PEDESTAL_ADVANCED;
		case COMPLEX -> PastelJEI.PEDESTAL_COMPLEX;
		};
	}

	public static Component getTitle(PedestalTier tier) {
		return switch(tier) {
		case BASIC -> TITLE_BASIC;
		case SIMPLE -> TITLE_SIMPLE;
		case ADVANCED -> TITLE_ADVANCED;
		case COMPLEX -> TITLE_COMPLEX;
		};
	}

	@Override
	public int getHeight() {
		return 89;
	}

	@Override
	public IDrawable getIcon() {
		return tier == PedestalTier.BASIC ? BASIC_ICON : null;
	}

	@Override
	public boolean isUnlocked(RecipeHolder<PedestalRecipe> recipeHolder) {
		return super.isUnlocked(recipeHolder) && recipeHolder.value().getTier().hasUnlocked(Minecraft.getInstance().player);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PedestalRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		PedestalRecipe recipe = recipeHolder.value();
		int powderSlotX = 1 + getWidth() / 2 - powderSlotCount * 9;
		List<IIngredientAcceptor<?>> gridSlots = new ArrayList<>(9);
		for(int y = 0; y < 3; ++y) {
			for(int x = 0; x < 3; ++x) {
				gridSlots.add(addSlot(builder, RecipeIngredientRole.INPUT, 7 + x * 18, 1 + y * 18, inputSlots[y * 3 + x], visible));
			}
		}
		List<IngredientStack> ingredients = recipe.getIngredientStacks();
		for(int i = 0; i < ingredients.size(); ++i) {
			gridSlots.get(recipe.getGridSlotId(i)).addItemStacks(ingredients.get(i).getItems().toList());
		}
		for(int i = 0; i < powderSlotCount; ++i) {
			IIngredientAcceptor<?> slot = addSlot(builder, RecipeIngredientRole.INPUT, powderSlotX + i * 18, 60, powderSlots[i], visible);
			GemstoneColor color = PastelGemstoneColor.values()[i];
			int powderAmount = recipe.getPowderInputs().getOrDefault(color, 0);
			if(powderAmount > 0) {
				slot.addItemStack(new ItemStack(color.getPowder(), powderAmount));
			}
		}
		addItem(builder, RecipeIngredientRole.OUTPUT, 101, 19, recipe.getResultItem(registryAccess()), outputSlot, visible);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<PedestalRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			PedestalRecipe recipe = recipeHolder.value();
			builder.addDrawable(tierOverlay, 88, 38);
			builder.addDrawable(JEIDrawables.recipeArrow(recipe.getCraftingTime() * 50), 67, 19);
			if(recipe.isShapeless()) {
				builder.addDrawable(JEIDrawables.SHAPELESS_ICON, 121, 0);
			}
		}
	}

	@Override
	public void draw(RecipeHolder<PedestalRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			PedestalRecipe recipe = recipeHolder.value();
			Font font = font();
			Component timeComponent = getTimeComponent(recipe.getCraftingTime(), recipe.getExperience());
			guiGraphics.drawString(font, timeComponent, getWidth() / 2 - font.width(timeComponent) / 2, 80, 0x3F3F3F, false);
		}
	}
}
