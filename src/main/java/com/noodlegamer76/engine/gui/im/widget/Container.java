package com.noodlegamer76.engine.gui.im.widget;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class Container extends Widget {
    private final List<Widget> widgets = new ArrayList<>();

    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void addWidget(Widget widget) {
        widgets.add(widget);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (Widget widget : widgets) {
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }
}
