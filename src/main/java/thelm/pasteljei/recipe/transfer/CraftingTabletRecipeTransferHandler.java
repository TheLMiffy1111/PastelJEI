package thelm.pasteljei.recipe.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import earth.terrarium.pastel.inventories.CraftingTabletScreenHandler;
import earth.terrarium.pastel.inventories.PastelScreenHandlerTypes;
import earth.terrarium.pastel.recipe.pedestal.PedestalRecipe;
import earth.terrarium.pastel.recipe.pedestal.PedestalTier;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeHolder;
import thelm.pasteljei.PastelJEI;

public class CraftingTabletRecipeTransferHandler implements IRecipeTransferHandler<CraftingTabletScreenHandler, RecipeHolder<PedestalRecipe>> {

	public final PedestalTier tier;
	public final RecipeType<RecipeHolder<PedestalRecipe>> recipeType;
	public final IRecipeTransferHandler<CraftingTabletScreenHandler, RecipeHolder<PedestalRecipe>> wrappedHandler;

	public CraftingTabletRecipeTransferHandler(PedestalTier tier, IRecipeTransferHandlerHelper transferHelper) {
		this.tier = tier;
		recipeType = getRecipeType(tier);
		wrappedHandler = transferHelper.createUnregisteredRecipeTransferHandler(new RecipeTransferInfo(recipeType));
	}

	public static RecipeType<RecipeHolder<PedestalRecipe>> getRecipeType(PedestalTier tier) {
		return switch(tier) {
		case BASIC -> PastelJEI.PEDESTAL_BASIC;
		case SIMPLE -> PastelJEI.PEDESTAL_SIMPLE;
		case ADVANCED -> PastelJEI.PEDESTAL_ADVANCED;
		case COMPLEX -> PastelJEI.PEDESTAL_COMPLEX;
		};
	}

	@Override
	public Class<CraftingTabletScreenHandler> getContainerClass() {
		return CraftingTabletScreenHandler.class;
	}

	@Override
	public Optional<MenuType<CraftingTabletScreenHandler>> getMenuType() {
		return Optional.of(PastelScreenHandlerTypes.CRAFTING_TABLET);
	}

	@Override
	public RecipeType<RecipeHolder<PedestalRecipe>> getRecipeType() {
		return recipeType;
	}

	@Override
	public IRecipeTransferError transferRecipe(CraftingTabletScreenHandler container, RecipeHolder<PedestalRecipe> recipeHolder, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		return wrappedHandler.transferRecipe(container, recipeHolder, () -> filterSlots(recipeSlots), player, maxTransfer, doTransfer);
	}

	public List<IRecipeSlotView> filterSlots(IRecipeSlotsView recipeSlots) {
		List<IRecipeSlotView> original = recipeSlots.getSlotViews();
		List<IRecipeSlotView> filtered = new ArrayList<>();
		int inputCount = 0;
		for(IRecipeSlotView slot : original) {
			if(slot.getRole() == RecipeIngredientRole.INPUT) {
				if(inputCount >= 9) {
					continue;
				}
				inputCount++;
			}
			filtered.add(slot);
		}
		return filtered;
	}

	public class RecipeTransferInfo extends GatedRecipeTransferInfo<CraftingTabletScreenHandler, PedestalRecipe> {

		public RecipeTransferInfo(RecipeType<RecipeHolder<PedestalRecipe>> recipeType) {
			super(CraftingTabletScreenHandler.class, PastelScreenHandlerTypes.CRAFTING_TABLET, recipeType, 0, 9, 15, 36);
		}

		@Override
		public boolean canHandle(CraftingTabletScreenHandler container, RecipeHolder<PedestalRecipe> recipeHolder) {
			return super.canHandle(container, recipeHolder) && container.getTier().orElse(PedestalTier.BASIC).compareTo(tier) >= 0;
		}
	}
}
