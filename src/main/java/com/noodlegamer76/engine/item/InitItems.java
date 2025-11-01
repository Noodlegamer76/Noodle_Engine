package com.noodlegamer76.engine.item;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.block.InitBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NoodleEngine.MODID);

    public static final DeferredItem<TestItem> TEST_ITEM = ITEMS.register("test_item",
            () -> new TestItem(new Item.Properties()));

    public static final DeferredItem<BlockItem> TEST_RENDERER = ITEMS.register("test_renderer",
            () -> new BlockItem(InitBlocks.TEST_RENDERER.get(), new Item.Properties()));

}
