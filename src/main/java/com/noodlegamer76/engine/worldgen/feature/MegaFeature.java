package com.noodlegamer76.engine.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MegaFeature extends Feature<NoneFeatureConfiguration> {
    public MegaFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        //ctx.level().setBlock(ctx.origin(), Blocks.GLASS.defaultBlockState(), 2);
        return false;
    }
}
