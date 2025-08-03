package thelm.pasteljei.ingredient.subtype;

import earth.terrarium.pastel.registries.PastelDataComponentTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;

public class UnstableItemSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		if(context == UidContext.Ingredient && ingredient.has(PastelDataComponentTypes.STABLE)) {
			return true;
		}
		return null;
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
		return getSubtypeData(ingredient, context) == null ? "" : "s";
	}
}
