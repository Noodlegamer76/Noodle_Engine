package com.noodlegamer76.engine.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RenderTesterEntity extends BlockEntity {
    public RenderTesterEntity(BlockPos pos, BlockState blockState) {
        super(InitBlockEntities.RENDER_TESTER.get(), pos, blockState);
    }
}
