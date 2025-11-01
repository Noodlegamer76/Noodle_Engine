package com.noodlegamer76.engine.gui.im.render;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface ImGuiRenderCall {
    void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
}
