package com.noodlegamer76.engine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.RenderableComponent;
import com.noodlegamer76.engine.entity.GameObject;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GameObjectRenderer extends EntityRenderer<GameObject> {
    private static final ResourceLocation DUMMY = ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "textures/block/dirt.png");

    public GameObjectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GameObject p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        for (Component component: p_entity.getComponentManager().getComponents().values()) {
            if (component instanceof RenderableComponent renderable) {
                renderable.render(partialTick);
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(GameObject gameObject) {
        return DUMMY;
    }
}
