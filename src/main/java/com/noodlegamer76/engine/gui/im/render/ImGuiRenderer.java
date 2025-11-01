package com.noodlegamer76.engine.gui.im.render;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class ImGuiRenderer {
    private static ImGuiRenderer INSTANCE;

    public static ImGuiRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImGuiRenderer();
        }
        return INSTANCE;
    }

    private final List<ImGuiRenderCall> calls = new ArrayList<>();

    public void addCall(ImGuiRenderCall call) {
        calls.add(call);
    }

    public void clear() {
        calls.clear();
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, int mouseButton, float partialTick) {
        handleClick(mouseX, mouseY, mouseButton);

        for (ImGuiRenderCall call : calls) {
            call.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        clear();
    }

    private void handleClick(int mouseX, int mouseY, int mouseButton) {
        //TODO: add click handling
    }
}
