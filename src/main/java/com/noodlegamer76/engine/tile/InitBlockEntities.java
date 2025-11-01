package com.noodlegamer76.engine.tile;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.block.InitBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.rmi.registry.Registry;

public class InitBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NoodleEngine.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RenderTesterEntity>> RENDER_TESTER = BLOCK_ENTITY_TYPES.register("render_tester",
            () -> BlockEntityType.Builder.of(RenderTesterEntity::new, InitBlocks.TEST_RENDERER.get()).build(null));
}
