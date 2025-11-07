package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.core.component.ComponentType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = NoodleEngine.MODID)
public class NoodleEngineRegistries {
    public static final ResourceKey<Registry<ComponentType<?>>> COMPONENT_TYPE = createRegistryKey("component_type");
    public static Registry<ComponentType<?>> COMPONENT_TYPES;

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, name));
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        COMPONENT_TYPES = event.create(new RegistryBuilder<>(COMPONENT_TYPE));
    }
}
