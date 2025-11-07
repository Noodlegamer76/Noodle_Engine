package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.renderer.entity.GameObjectRenderer;
import com.noodlegamer76.engine.client.renderer.tile.TestRendererRenderer;
import com.noodlegamer76.engine.entity.InitEntities;
import com.noodlegamer76.engine.tile.InitBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NoodleEngine.MODID)
public class RegisterRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(InitEntities.GAME_OBJECT.get(), GameObjectRenderer::new);

        event.registerBlockEntityRenderer(InitBlockEntities.RENDER_TESTER.get(), TestRendererRenderer::new);
    }
}
