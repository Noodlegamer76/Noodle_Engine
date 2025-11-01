package com.noodlegamer76.engine.block;

import com.noodlegamer76.engine.NoodleEngine;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NoodleEngine.MODID);

    public static final DeferredBlock<TestRendererBlock> TEST_RENDERER = BLOCKS.register("test_renderer",
    () -> new TestRendererBlock(BlockBehaviour.Properties.of().noOcclusion().noCollission()));
}
