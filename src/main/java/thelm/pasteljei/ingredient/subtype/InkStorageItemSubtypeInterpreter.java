package thelm.pasteljei.ingredient.subtype;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import earth.terrarium.pastel.api.energy.InkStorageItem;
import earth.terrarium.pastel.api.energy.color.InkColor;
import earth.terrarium.pastel.registries.PastelRegistries;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;

public class InkStorageItemSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		if(context == UidContext.Ingredient && ingredient.getItem() instanceof InkStorageItem inkStorageItem) {
			Map<InkColor, Long> storage = inkStorageItem.getEnergyStorage(ingredient).getEnergy();
			List<InkColor> colors = PastelRegistries.INK_COLOR.stream().
					filter(color -> storage.getOrDefault(color, 0L) > 0).
					toList();
			return colors.isEmpty() ? null : colors;
		}
		return "";
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
		if(context == UidContext.Ingredient && ingredient.getItem() instanceof InkStorageItem inkStorageItem) {
			Map<InkColor, Long> storage = inkStorageItem.getEnergyStorage(ingredient).getEnergy();
			return PastelRegistries.INK_COLOR.stream().
					filter(color -> storage.getOrDefault(color, 0L) > 0).
					map(Objects::toString).
					collect(Collectors.joining(","));
		}
		return "";
	}
}
