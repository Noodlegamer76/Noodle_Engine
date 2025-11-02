package com.noodlegamer76.engine.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMeshes;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.ModelStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        McGltf model = ModelStorage.getModel(ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "gltf/master.glb"));
        if (level.isClientSide) {

            Vec3 position = player.position();
            RenderableMeshes.removeAll();
            //for (int i = 0; i < 400; i++) {
            //    Vec3 offset = new Vec3((Math.random() - 0.5) * 25, (Math.random() - 0.5) * 25, (Math.random() - 0.5) * 25);
            //    Vec3 position = new Vec3(player.getX() + offset.x, player.getY() + offset.y, player.getZ() + offset.z);
            //
            //}
            for (MeshData meshData : model.getMeshes()) {
                PoseStack poseStack = new PoseStack();
                poseStack.pushPose();

                poseStack.translate(position.x, position.y, position.z);
                poseStack.scale(1, 1, 1);

                GlbRenderer.addInstance(meshData, poseStack, -1);

                poseStack.popPose();
            }

        }

        return super.use(level, player, usedHand);
    }
}
