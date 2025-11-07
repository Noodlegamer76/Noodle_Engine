package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMeshes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = NoodleEngine.MODID)
public class WorldEvents {

    @SubscribeEvent
    public static void onLeaveWorld(LevelEvent.Unload event) {
        RenderableMeshes.removeAll();
    }
}
