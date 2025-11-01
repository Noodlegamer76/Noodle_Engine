package com.noodlegamer76.engine.mixin.accessor;

import com.mojang.blaze3d.vertex.VertexBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VertexBuffer.class)
public interface VertexBufferAccessor {

    @Accessor(value = "indexCount")
    int getIndexCount();
}
