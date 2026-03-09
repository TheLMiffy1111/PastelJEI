package thelm.pasteljei.recipe.category;

import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import earth.terrarium.pastel.PastelCommon;
import earth.terrarium.pastel.items.magic_items.KnowledgeGemItem;
import earth.terrarium.pastel.recipe.enchanter.EnchantmentUpgradeRecipe;
import earth.terrarium.pastel.registries.PastelAdvancements;
import earth.terrarium.pastel.registries.PastelBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.inputs.IJeiGuiEventListener;
import mezz.jei.api.gui.inputs.RecipeSlotUnderMouse;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.ISlottedRecipeWidget;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.pasteljei.PastelJEI;

/**
 * Based on EnchantmentUpgradeEmiRecipeGated
 */
public class EnchantmentUpgradeRecipeCategory extends AbstractGatedRecipeCategory<EnchantmentUpgradeRecipe> {

	public static final Component TITLE = Component.translatable("container.pastel.rei.enchantment_upgrading.title");
	public static final Component TOOLTIP = Component.translatable("container.pastel.rei.enchantment_upgrade.tooltip").withColor(0xDB3564);
	public static final Component BUTTON = Component.translatable("container.pastel.rei.enchantment_upgrade.button");

	public static final ResourceLocation BACKGROUND = PastelCommon.locate("textures/gui/container/enchanter.png");
	public static final ResourceDrawable ALTAR = new ResourceDrawable(BACKGROUND, 0, 0, 54, 54);
	public static final ResourceDrawable OVERENCHANT = new ResourceDrawable(BACKGROUND, 64, 0, 16, 16);
	public static final ResourceDrawable MINUS = new ResourceDrawable(BACKGROUND, 64, 16, 8, 8);
	public static final ResourceDrawable PLUS = new ResourceDrawable(BACKGROUND, 72, 16, 8, 8);

	public EnchantmentUpgradeRecipeCategory() {
		super(PastelJEI.ENCHANTMENT_UPGRADE, TITLE);
	}

	@Override
	public int getHeight() {
		return 80;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<EnchantmentUpgradeRecipe> recipeHolder, IFocusGroup focuses) {
		boolean visible = isVisible(recipeHolder);
		EnchantmentUpgradeRecipe recipe = recipeHolder.value();
		Item inputItem = recipe.getBulkItem();
		int inputCount = recipe.getBaseItemCost();
		addItem(builder, RecipeIngredientRole.INPUT, 113, 7, KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getBaseXPCost(), true), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.CATALYST, 113, 53, PastelBlocks.ENCHANTER.toStack(), visible);
		addSlot(builder, RecipeIngredientRole.INPUT, 34, 32, JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 21, 1, new ItemStack(inputItem, getSplitCount(inputCount, 0)), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 47, 1, new ItemStack(inputItem, getSplitCount(inputCount, 1)), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 65, 19, new ItemStack(inputItem, getSplitCount(inputCount, 2)), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 65, 45, new ItemStack(inputItem, getSplitCount(inputCount, 3)), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 47, 63, new ItemStack(inputItem, getSplitCount(inputCount, 4)), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 21, 63, new ItemStack(inputItem, getSplitCount(inputCount, 5)), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 3, 45, new ItemStack(inputItem, getSplitCount(inputCount, 6)), JEIDrawables.SLOT, visible);
		addItem(builder, RecipeIngredientRole.INPUT, 3, 19, new ItemStack(inputItem, getSplitCount(inputCount, 7)), JEIDrawables.SLOT, visible);
		addSlot(builder, RecipeIngredientRole.OUTPUT, 113, 32, JEIDrawables.OUTPUT_SLOT, visible);
		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(IntStream.range(1, recipe.getLevelCap()).mapToObj(levelToBook(recipe.getEnchantment())).toList());
		builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStacks(IntStream.rangeClosed(2, recipe.getLevelCap()).mapToObj(levelToBook(recipe.getEnchantment())).toList());
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<EnchantmentUpgradeRecipe> recipeHolder, IFocusGroup focuses) {
		if(isVisible(recipeHolder)) {
			EnchantmentUpgradeRecipe recipe = recipeHolder.value();
			builder.addDrawable(JEIDrawables.RECIPE_ARROW, 84, 32);
			builder.addDrawable(MINUS, 86, 20);
			builder.addDrawable(PLUS, 96, 20);
			List<IRecipeSlotDrawable> slots = List.copyOf(builder.getRecipeSlots().getSlots());
			int index = IntStream.concat(
					focuses.getItemStackFocuses(RecipeIngredientRole.INPUT).
					map(f -> f.getTypedValue().getIngredient()).
					mapToInt(bookToLevel(recipe.getEnchantment())),
					focuses.getItemStackFocuses(RecipeIngredientRole.OUTPUT).
					map(f -> f.getTypedValue().getIngredient()).
					mapToInt(bookToLevel(recipe.getEnchantment())).
					map(l -> l - 1)).
					filter(l -> l > 0).min().orElse(1);
			StateHandler stateHandler = new StateHandler(recipe, slots, index);
			builder.addSlottedWidget(stateHandler, slots);
			builder.addGuiEventListener(new StateButton(new ScreenRectangle(86, 20, 8, 8), stateHandler, false));
			builder.addGuiEventListener(new StateButton(new ScreenRectangle(96, 20, 8, 8), stateHandler, true));
		}
	}

	@Override
	public void draw(RecipeHolder<EnchantmentUpgradeRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		super.draw(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		if(isVisible(recipeHolder)) {
			ALTAR.draw(guiGraphics, 15, 13);
			EnchantmentUpgradeRecipe recipe = recipeHolder.value();
			if(recipe.getLevelCap() > recipe.getEnchantment().value().getMaxLevel() &&
					hasAdvancement(PastelAdvancements.Milestones.UNLOCK_OVERENCHANTING_WITH_ENCHANTER)) {
				OVERENCHANT.draw(guiGraphics, 3, 1);
			}
		}
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<EnchantmentUpgradeRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if(isVisible(recipeHolder)) {
			EnchantmentUpgradeRecipe recipe = recipeHolder.value();
			if(recipe.getLevelCap() > recipe.getEnchantment().value().getMaxLevel() &&
					hasAdvancement(PastelAdvancements.Milestones.UNLOCK_OVERENCHANTING_WITH_ENCHANTER) &&
					mouseX >= 3 && mouseX < 19 && mouseY >= 1 && mouseY < 17) {
				tooltip.add(TOOLTIP);
			}
			if((mouseX >= 86 && mouseX < 94 || mouseX >= 96 && mouseX < 104) && mouseY >= 20 && mouseY < 28) {
				tooltip.add(BUTTON);
			}
		}
	}

	public int getSplitCount(int inputCount, int index) {
		return inputCount / 8 + (index < inputCount % 8 ? 1 : 0);
	}

	public IntFunction<ItemStack> levelToBook(Holder<Enchantment> enchantment) {
		return level -> {
			ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
			ItemEnchantments.Mutable itemEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
			itemEnchantments.set(enchantment, level);
			book.set(DataComponents.STORED_ENCHANTMENTS, itemEnchantments.toImmutable());
			return book;
		};
	}

	public ToIntFunction<ItemStack> bookToLevel(Holder<Enchantment> enchantment) {
		return stack -> stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantment);
	}

	public class StateHandler implements ISlottedRecipeWidget {

		static final ScreenPosition ZERO = new ScreenPosition(0, 0);
		final EnchantmentUpgradeRecipe recipe;
		final List<IRecipeSlotDrawable> slots;
		int index = 1;

		public StateHandler(EnchantmentUpgradeRecipe recipe, List<IRecipeSlotDrawable> slots, int index) {
			this.recipe = recipe;
			this.slots = slots;
			this.index = index;
			updateSlots();
		}

		@Override
		public ScreenPosition getPosition() {
			return ZERO;
		}

		public boolean increase() {
			int cap = hasAdvancement(PastelAdvancements.Milestones.UNLOCK_OVERENCHANTING_WITH_ENCHANTER) ? recipe.getLevelCap() : recipe.getEnchantment().value().getMaxLevel();
			int newIndex = Math.clamp(index + 1, 1, cap - 1);
			if(index == newIndex) {
				return false;
			}
			index = newIndex;
			updateSlots();
			return true;
		}

		public boolean decrease() {
			int cap = hasAdvancement(PastelAdvancements.Milestones.UNLOCK_OVERENCHANTING_WITH_ENCHANTER) ? recipe.getLevelCap() : recipe.getEnchantment().value().getMaxLevel();
			int newLevel = Math.clamp(index - 1, 1, cap - 1);
			if(index == newLevel) {
				return false;
			}
			index = newLevel;
			updateSlots();
			return true;
		}

		public void updateSlots() {
			slots.forEach(IRecipeSlotDrawable::clearDisplayOverrides);
			slots.get(0).createDisplayOverrides().addItemStack(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getXpScaling().apply(index), false));
			slots.get(2).createDisplayOverrides().addItemStack(levelToBook(recipe.getEnchantment()).apply(index));
			slots.get(11).createDisplayOverrides().addItemStack(levelToBook(recipe.getEnchantment()).apply(index + 1));
			Item inputItem = recipe.getBulkItem();
			int inputCount = recipe.getItemScaling().apply(index);
			for(int i = 0; i < 8; ++i) {
				slots.get(3 + i).createDisplayOverrides().addItemStack(new ItemStack(inputItem, getSplitCount(inputCount, i)));
			}
		}

		@Override
		public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
			for(IRecipeSlotDrawable slot : slots) {
				slot.draw(guiGraphics);
			}
			Font font = font();
			Component levelComponent = Component.translatable("container.pastel.rei.enchantment_upgrade.level", index, index + 1);
			Component reqComponent = Component.translatable("container.pastel.rei.enchantment_upgrade.required_item_count", recipe.getItemScaling().apply(index));
			guiGraphics.drawString(font, levelComponent, 69, 2, index >= recipe.getEnchantment().value().getMaxLevel() ? 0xDB3564 : 0x3F3F3F, false);
			guiGraphics.drawString(font, reqComponent, 69, 71, 0x3F3F3F, false);
		}

		@Override
		public Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY) {
			for(IRecipeSlotDrawable slot : slots) {
				if(slot.isMouseOver(mouseX, mouseY)) {
					return Optional.of(new RecipeSlotUnderMouse(slot, ZERO));
				}
			}
			return Optional.empty();
		}
	}

	public class StateButton implements IJeiGuiEventListener {

		final ScreenRectangle area;
		final StateHandler handler;
		final boolean increase;

		public StateButton(ScreenRectangle area, StateHandler handler, boolean increase) {
			this.area = area;
			this.handler = handler;
			this.increase = increase;
		}

		@Override
		public ScreenRectangle getArea() {
			return area;
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			boolean clicked = increase ? handler.increase() : handler.decrease();
			if(clicked) {
				Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
			}
			return clicked;
		}
	}
}
