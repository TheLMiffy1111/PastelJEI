package thelm.pasteljei.recipe.category;

import earth.terrarium.pastel.api.recipe.GatedRecipe;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

public abstract class AbstractGatedRecipeCategory<R extends GatedRecipe<?>> extends AbstractUnlockableRecipeCategory<RecipeHolder<R>> {

	public AbstractGatedRecipeCategory(RecipeType<RecipeHolder<R>> recipeType, Component title) {
		super(recipeType, title);
	}

	@Override
	public boolean isUnlocked(RecipeHolder<R> recipeHolder) {
		return hasAdvancement(recipeHolder.value().getRecipeTypeUnlockIdentifier()) && hasAdvancement(recipeHolder.value().getRequiredAdvancementIdentifier().orElse(null));
	}

	@Override
	public boolean isVisible(RecipeHolder<R> recipeHolder) {
		return super.isVisible(recipeHolder) && !recipeHolder.value().isSecret();
	}

	@Override
	public void draw(RecipeHolder<R> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		if(!isUnlocked(recipeHolder)) {
			drawLockedText(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		}
		else if(recipeHolder.value().isSecret()) {
			drawSecretText(recipeHolder, recipeSlotsView, guiGraphics, mouseX, mouseY);
		}
	}

	public void drawSecretText(RecipeHolder<R> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		Font font = font();
		if(recipeHolder.value().getSecretHintText(recipeHolder.id()) == null) {
			guiGraphics.drawString(font, SECRET, getWidth() / 2 - font.width(SECRET) / 2, getHeight() / 2 - 4, 0x3F3F3F, false);
		}
		else {
			Component hintComponent = recipeHolder.value().getSecretHintText(recipeHolder.id());
			guiGraphics.drawString(font, SECRET_HINT, getWidth() / 2 - font.width(SECRET_HINT) / 2, getHeight() / 2 - 9, 0x3F3F3F, false);
			guiGraphics.drawString(font, hintComponent, getWidth() / 2 - font.width(hintComponent) / 2, getHeight() / 2 + 1, 0x3F3F3F, false);
		}
	}
}
