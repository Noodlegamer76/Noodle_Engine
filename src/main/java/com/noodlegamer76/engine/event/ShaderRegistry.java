package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.ModVertexFormat;
import com.noodlegamer76.engine.gltf.material.ShaderReference;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = NoodleEngine.MODID)
public class ShaderRegistry {
    public static ShaderReference pbr = new ShaderReference();

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {

        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "pbr"),
                        ModVertexFormat.GLB_PBR),
                (e) -> pbr.shader = e);

    }
}