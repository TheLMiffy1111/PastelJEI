package thelm.pasteljei.gui.handler;

import java.util.List;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;

public class OverlayHidingExtraAreaHandler<T extends AbstractContainerScreen<?>> implements IGuiContainerHandler<T> {

	@Override
	public List<Rect2i> getGuiExtraAreas(T containerScreen) {
		return List.of(new Rect2i(0, 0, containerScreen.width, containerScreen.height));
	}
}
