package thelm.pasteljei.recipe.category;

import java.util.List;

import com.cmdpro.databank.DatabankUtils;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import thelm.pasteljei.PastelJEI;

public abstract class AbstractUnlockableRecipeCategory<R> implements IRecipeCategory<R> {

	public static final Component HIDDEN_LINE_1 = Component.translatable("container.pastel.rei.pedestal_crafting.recipe_not_unlocked_line_1");
	public static final Component HIDDEN_LINE_2 = Component.translatable("container.pastel.rei.pedestal_crafting.recipe_not_unlocked_line_2");
	public static final Component SECRET = Component.translatable("container.pastel.rei.pedestal_crafting.secret_recipe");
	public static final Component SECRET_HINT = Component.translatable("container.pastel.rei.pedestal_crafting.secret_recipe.hint");

	public final RecipeType<R> recipeType;
	public final Component title;

	public AbstractUnlockableRecipeCategory(RecipeType<R> recipeType, Component title) {
		this.recipeType = recipeType;
		this.title = title;
	}

	@Override
	public RecipeType<R> getRecipeType() {
		return recipeType;
	}

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public int getWidth() {
		return 136;
	}

	@Override
	public abstract int getHeight();

	@Override
	public IDrawable getIcon() {
		return null;
	}

	public abstract boolean isUnlocked(R recipe);

	public boolean isVisible(R recipe) {
		return isUnlocked(recipe);
	}

	@Override
	public void draw(R recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		if(!isUnlocked(recipe)) {
			drawLockedText(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
		}
	}

	public void drawLockedText(R recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		Font font = font();
		guiGraphics.drawString(font, HIDDEN_LINE_1, getWidth() / 2 - font.width(HIDDEN_LINE_1) / 2, getHeight() / 2 - 9, 0x3F3F3F, false);
		guiGraphics.drawString(font, HIDDEN_LINE_2, getWidth() / 2 - font.width(HIDDEN_LINE_2) / 2, getHeight() / 2 + 1, 0x3F3F3F, false);
	}

	public boolean hasAdvancement(ResourceLocation advancement) {
		return advancement == null || DatabankUtils.hasAdvancementClient(advancement);
	}

	public RegistryAccess registryAccess() {
		return Minecraft.getInstance().level.registryAccess();
	}

	public Font font() {
		return Minecraft.getInstance().font;
	}

	public IJeiHelpers jeiHelpers() {
		return PastelJEI.jeiHelpers;
	}

	public IPlatformFluidHelper<?> fluidHelper() {
		return jeiHelpers().getPlatformFluidHelper();
	}

	public IGuiHelper guiHelper() {
		return jeiHelpers().getGuiHelper();
	}

	public IIngredientAcceptor<?> addSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, IDrawable background, boolean visible) {
		return visible ? builder.addSlot(ingredientRole, x, y).setBackground(background, 8 - background.getWidth() / 2, 8 - background.getHeight() / 2) : builder.addInvisibleIngredients(ingredientRole);
	}

	public IIngredientAcceptor<?> addSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, boolean visible) {
		return visible ? builder.addSlot(ingredientRole, x, y) : builder.addInvisibleIngredients(ingredientRole);
	}

	public IIngredientAcceptor<?> addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, List<ItemStack> itemStacks, IDrawable background, boolean visible) {
		return addSlot(builder, ingredientRole, x, y, background, visible).addItemStacks(itemStacks);
	}

	public IIngredientAcceptor<?> addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, List<ItemStack> itemStacks, boolean visible) {
		return addSlot(builder, ingredientRole, x, y, visible).addItemStacks(itemStacks);
	}

	public IIngredientAcceptor<?> addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, Ingredient ingredient, IDrawable background, boolean visible) {
		return addSlot(builder, ingredientRole, x, y, background, visible).addIngredients(ingredient);
	}

	public IIngredientAcceptor<?> addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, Ingredient ingredient, boolean visible) {
		return addSlot(builder, ingredientRole, x, y, visible).addIngredients(ingredient);
	}

	public IIngredientAcceptor<?> addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, ItemStack itemStack, IDrawable background, boolean visible) {
		return addSlot(builder, ingredientRole, x, y, background, visible).addItemStack(itemStack);
	}

	public IIngredientAcceptor<?> addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, ItemStack itemStack, boolean visible) {
		return addSlot(builder, ingredientRole, x, y, visible).addItemStack(itemStack);
	}

	public IIngredientAcceptor<?> addFluid(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, FluidIngredient ingredient, long amount, IDrawable background, boolean visible) {
		IIngredientAcceptor<?> acceptor = addSlot(builder, ingredientRole, x, y, background, visible);
		for(FluidStack stack : ingredient.getStacks()) {
			acceptor.addFluidStack(stack.getFluid(), amount, stack.getComponentsPatch());
		}
		if(acceptor instanceof IRecipeSlotBuilder slot) {
			slot.setFluidRenderer(amount, false, 16, 16);
		}
		return acceptor;
	}

	public Component getTimeComponent(int time) {
		return time == 20 ? Component.translatable("container.pastel.rei.crafting_time_one_second", 1) : Component.translatable("container.pastel.rei.crafting_time", time / 20);
	}

	public Component getTimeComponent(int time, float experience) {
		return time == 20 ? Component.translatable("container.pastel.rei.crafting_time_one_second_and_xp", 1, experience) : Component.translatable("container.pastel.rei.crafting_time_and_xp", time / 20, experience);
	}
}
