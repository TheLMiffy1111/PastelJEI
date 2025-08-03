package thelm.pasteljei.ingredient.subtype;

import java.util.List;

import earth.terrarium.pastel.capabilities.ExperienceHandler;
import earth.terrarium.pastel.capabilities.PastelCapabilities;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;

public class ExperienceStorageItemSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		HolderLookup.Provider registries = Minecraft.getInstance().level.registryAccess();
		if(context == UidContext.Ingredient) {
			ExperienceHandler xpHandler = ingredient.getCapability(PastelCapabilities.Misc.XP, registries);
			if(xpHandler != null && xpHandler.getCapacity() > 0) {
				int capacity = xpHandler.getCapacity();
				if(xpHandler.getStoredAmount() >= capacity) {
					return List.of(capacity, true);
				}
				return capacity;
			}
		}
		return null;
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
		HolderLookup.Provider registries = Minecraft.getInstance().level.registryAccess();
		if(context == UidContext.Ingredient) {
			ExperienceHandler xpHandler = ingredient.getCapability(PastelCapabilities.Misc.XP, registries);
			if(xpHandler != null && xpHandler.getCapacity() > 0) {
				int capacity = xpHandler.getCapacity();
				if(xpHandler.getStoredAmount() >= capacity) {
					return capacity + ",f";
				}
				return Integer.toString(capacity);
			}
		}
		return "";
	}
}
