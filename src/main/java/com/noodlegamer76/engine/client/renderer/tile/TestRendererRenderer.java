package com.noodlegamer76.engine.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.ModelStorage;
import com.noodlegamer76.engine.tile.RenderTesterEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class TestRendererRenderer implements BlockEntityRenderer<RenderTesterEntity> {
    McGltf model;

    public TestRendererRenderer(BlockEntityRendererProvider.Context context) {
        ModelStorage.getModels().forEach((location, model) -> {
            System.out.println(location);
        });
        model = ModelStorage.getModel(ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "gltf/bathroom.glb"));
    }

    @Override
    public void render(RenderTesterEntity renderTesterEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {

        PoseStack stack = new PoseStack();
        stack.pushPose();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Quaternionf rotation = camera.rotation();
        stack.mulPose(new Quaternionf(rotation).invert());
        Vec3 cameraPos = camera.getPosition();
        stack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        BlockPos pos = renderTesterEntity.getBlockPos();
        stack.translate(pos.getX(), pos.getY(), pos.getZ());
        //stack.scale(1.5f, 1.5f, 1.5f);

        for (MeshData meshData : model.getMeshes()) {
            GlbRenderer.addInstance(meshData, stack, i);
        }

        stack.popPose();
    }

    @Override
    public boolean shouldRender(RenderTesterEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(RenderTesterEntity blockEntity) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(RenderTesterEntity blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity).inflate(9999);
    }
}
