package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.gltf.load.GltfLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = NoodleEngine.MODID)
public class SetupEvents {

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String modelPath = "gltf";
        GltfLoader.loadAllGlbModels(resourceManager, modelPath);
    }
}
