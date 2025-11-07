package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.network.GameObjectPayload.ComponentHandler;
import com.noodlegamer76.engine.network.GameObjectPayload.ComponentPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = NoodleEngine.MODID)
public class RegisterPayloads {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                ComponentPayload.TYPE,
                ComponentPayload.STREAM_CODEC,
                ComponentHandler::handle
        );
    }
}
