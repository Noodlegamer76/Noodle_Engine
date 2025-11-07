package com.noodlegamer76.engine.core.component;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.event.NoodleEngineRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitComponents {
    public static final DeferredRegister<ComponentType<?>> COMPONENT_TYPES = DeferredRegister.create(NoodleEngineRegistries.COMPONENT_TYPE, NoodleEngine.MODID);

    public static final DeferredHolder<ComponentType<?>, ComponentType<GltfModelComponent>> GLTF_MODEL = COMPONENT_TYPES.register("gltf_model",
            () -> new ComponentType<>(GltfModelComponent::new));

    @FunctionalInterface
    public interface ComponentSupplier<T extends Component> {
        T create(GameObject var1);
    }

    public static ComponentType<?> getComponentType(ResourceLocation id) {
        return InitComponents.COMPONENT_TYPES.getEntries().stream()
                .filter(holder -> holder.getId().equals(id))
                .map(DeferredHolder::get)
                .findFirst()
                .orElse(null);
    }
}
