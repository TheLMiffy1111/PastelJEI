package thelm.pasteljei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public class MemoryItemSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		if(ingredient.has(DataComponents.ENTITY_DATA)) {
			return ingredient.get(DataComponents.ENTITY_DATA).copyTag().getString("id");
		}
		return null;
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
		if(ingredient.has(DataComponents.ENTITY_DATA)) {
			return ingredient.get(DataComponents.ENTITY_DATA).copyTag().getString("id");
		}
		return "";
	}
}
