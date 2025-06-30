package thelm.pasteljei.recipe;

import java.util.List;

import mezz.jei.api.helpers.IPlatformFluidHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import thelm.pasteljei.PastelJEI;

public record BlockConversionRecipe(BlockState input, BlockState output) {

	public BlockConversionRecipe(Block input, BlockState output) {
		this(input.defaultBlockState(), output);
	}

	public List<?> inputIngredient() {
		return toIngredient(input);
	}

	public List<?> outputIngredient() {
		return toIngredient(output);
	}

	public boolean isViewable() {
		return !inputIngredient().isEmpty() && !outputIngredient().isEmpty();
	}

	public static List<?> toIngredient(BlockState state) {
		if(state.getBlock() instanceof LiquidBlock) {
			IPlatformFluidHelper<?> fluidHelper = PastelJEI.jeiHelpers.getPlatformFluidHelper();
			Fluid fluid = state.getFluidState().getType();
			if(fluid instanceof FlowingFluid fFluid) {
				fluid = fFluid.getSource();
			}
			return List.of(fluidHelper.create(fluid.builtInRegistryHolder(), fluidHelper.bucketVolume()));
		}
		else {
			ItemStack stack = new ItemStack(state.getBlock());
			return stack.isEmpty() ? List.of() : List.of(stack);
		}
	}
}
