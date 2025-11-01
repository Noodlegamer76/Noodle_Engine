package com.noodlegamer76.engine.worldgen.feature;

import com.noodlegamer76.engine.NoodleEngine;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, NoodleEngine.MODID);

    public static final DeferredHolder<Feature<?>, MegaFeature> MEGA_FEATURE = FEATURES.register("mega_structure",
            () -> new MegaFeature(NoneFeatureConfiguration.CODEC));
}
