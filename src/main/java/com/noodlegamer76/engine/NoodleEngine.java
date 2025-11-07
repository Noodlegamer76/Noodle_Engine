package com.noodlegamer76.engine;

import com.mojang.logging.LogUtils;
import com.noodlegamer76.engine.block.InitBlocks;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.entity.InitEntities;
import com.noodlegamer76.engine.item.InitItems;
import com.noodlegamer76.engine.tile.InitBlockEntities;
import com.noodlegamer76.engine.worldgen.feature.InitFeatures;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(NoodleEngine.MODID)
public class NoodleEngine {
    public static final String MODID = "noodleengine";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NoodleEngine(IEventBus modEventBus, ModContainer modContainer) {
        InitItems.ITEMS.register(modEventBus);
        InitFeatures.FEATURES.register(modEventBus);
        InitBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        InitBlocks.BLOCKS.register(modEventBus);
        InitComponents.COMPONENT_TYPES.register(modEventBus);
        InitEntities.ENTITY_TYPES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
