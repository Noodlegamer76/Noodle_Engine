package com.noodlegamer76.engine.gui;

import com.noodlegamer76.engine.gui.im.interaction.CursorStack;
import com.noodlegamer76.engine.gui.im.render.ImGuiRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class ImGuiScreen extends Screen {
    public ImGuiScreen() {
        super(Component.empty());
    }

    @Override
    public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderImGui(guiGraphics, mouseX, mouseY, partialTick);

        ImGuiRenderer.getInstance().render(guiGraphics, mouseX, mouseY, mouseX, partialTick);
    }

    public abstract void renderImGui(GuiGraphics graphics, int mouseX, int mouseY, float partialTick);

    @Override
    public void onClose() {
        super.onClose();
        ImGuiRenderer.getInstance().clear();
        CursorStack.clear();
    }
}
