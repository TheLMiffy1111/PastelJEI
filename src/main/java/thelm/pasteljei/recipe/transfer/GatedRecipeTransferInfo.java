package thelm.pasteljei.recipe.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import earth.terrarium.pastel.api.recipe.GatedRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeHolder;

public class GatedRecipeTransferInfo<C extends AbstractContainerMenu, R extends GatedRecipe<?>> implements IRecipeTransferInfo<C, RecipeHolder<R>> {

	public final Class<? extends C> containerClass;
	public final MenuType<C> menuType;
	public final RecipeType<RecipeHolder<R>> recipeType;
	public final int recipeSlotStart;
	public final int recipeSlotCount;
	public final int inventorySlotStart;
	public final int inventorySlotCount;

	public GatedRecipeTransferInfo(Class<? extends C> containerClass, MenuType<C> menuType, RecipeType<RecipeHolder<R>> recipeType, int recipeSlotStart, int recipeSlotCount, int inventorySlotStart, int inventorySlotCount) {
		this.containerClass = containerClass;
		this.menuType = menuType;
		this.recipeType = recipeType;
		this.recipeSlotStart = recipeSlotStart;
		this.recipeSlotCount = recipeSlotCount;
		this.inventorySlotStart = inventorySlotStart;
		this.inventorySlotCount = inventorySlotCount;
	}

	@Override
	public Class<? extends C> getContainerClass() {
		return containerClass;
	}

	@Override
	public Optional<MenuType<C>> getMenuType() {
		return Optional.ofNullable(menuType);
	}

	@Override
	public RecipeType<RecipeHolder<R>> getRecipeType() {
		return recipeType;
	}

	@Override
	public boolean canHandle(C container, RecipeHolder<R> recipeHolder) {
		return isVisible(recipeHolder);
	}

	@Override
	public List<Slot> getRecipeSlots(C container, RecipeHolder<R> recipeHolder) {
		List<Slot> slots = new ArrayList<>(recipeSlotCount);
		for(int i = recipeSlotStart; i < recipeSlotStart + recipeSlotCount; i++) {
			Slot slot = container.getSlot(i);
			slots.add(slot);
		}
		return slots;
	}

	@Override
	public List<Slot> getInventorySlots(C container, RecipeHolder<R> recipeHolder) {
		List<Slot> slots = new ArrayList<>(inventorySlotCount);
		for(int i = inventorySlotStart; i < inventorySlotStart + inventorySlotCount; i++) {
			Slot slot = container.getSlot(i);
			slots.add(slot);
		}
		return slots;
	}

	public boolean isUnlocked(RecipeHolder<R> recipeHolder) {
		return hasAdvancement(recipeHolder.value().getRecipeTypeUnlockIdentifier()) && hasAdvancement(recipeHolder.value().getRequiredAdvancementIdentifier().orElse(null));
	}

	public boolean isVisible(RecipeHolder<R> recipeHolder) {
		return isUnlocked(recipeHolder) && !recipeHolder.value().isSecret();
	}

	public boolean hasAdvancement(ResourceLocation advancement) {
		return advancement == null || AdvancementHelper.hasAdvancementClient(advancement);
	}
}
